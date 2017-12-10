package net.yapbam.gui;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import com.fathzer.soft.ajlib.utilities.FileUtils;

import net.yapbam.util.CheckSum;
import net.yapbam.util.Portable;

/** This class is in charge of removing obsolete files from previous Yapbam installations. 
 */
class FolderCleaner {
	private FolderCleaner(){}
/*
	public static void main (String[] args) {
		File globalFolder = new File("C:/Users/Jean-Marc/Desktop/YapbamOld");
		String[] foldersName = new String[]{"0.4.0","0.6.2","0.6.3","0.6.4","0.6.5","0.6.6","0.6.7","0.7.0","0.7.1","0.7.2","0.7.3"};

		// This was used to build the obsolete file table.
		try {
			String[] paths = new String[]{"YapbamDataSample_en.xml","YapbamDataSample_fr.xml","Installation instructions.html",
					"contributors.html","license.html","Release notes.html","help/import.html","help/regexp.html","help/fr/import.html",
					"help/fr/regexp.html","yapbam.jar","yapbam.bat"};
			
			File[] folders = new File[foldersName.length];
			for (int i=0; i<folders.length;i++) {
				folders[i] = new File(globalFolder, foldersName[i]);
			}
			buildProperties(paths, folders);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		// this was used to test cleaning on a lot of old versions.
		for (String folder : foldersName) {
			clean (new File(globalFolder, folder));
		}
	}
/**/
	/** Outputs to standard output a property file in the format expected by the clean method.
	 * @param paths The relative paths of the files to delete
	 * @param folders the folders that contain different versions of these files.
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	static void buildProperties(String[] paths, File[] folders) throws IOException {
		for (String filePath : paths) {
			String[] checksums = CheckSum.getCheckSums(folders, filePath);
			if (checksums.length>0) {
				System.out.print (URLEncoder.encode(filePath)+"="); //$NON-NLS-1$
				for (int i = 0; i < checksums.length; i++) {
					if (i!=0) {
						System.out.print(',');
					}
					System.out.print(checksums[i]);
				}
				System.out.println();
			}
		}
	}
	
	/** Cleans an installation folder.
	 * <br>This method may be used to test the cleaner on an old installation without installing yapbam over it.
	 * @param installationFolder The folder to clean.
	 */
	@SuppressWarnings("deprecation")
	private static void clean(File installationFolder) {
		try {
			Collection<CleaningJob> jobs = new ArrayList<FolderCleaner.CleaningJob>();
			// Files we need to remove are stored in a resource bundle file
			ResourceBundle checksumsMap = ResourceBundle.getBundle(FolderCleaner.class.getPackage().getName()+".obsoleteFilesChecksums"); //$NON-NLS-1$
			for (String path : checksumsMap.keySet()) { // For every obsolete files
				jobs.add(new CleaningJob(new File(installationFolder, URLDecoder.decode(path)), checksumsMap.getString(path).split(","), null)); //$NON-NLS-1$
			}
			// Files we need to move ... are defined ... nowhere but below
			jobs.add(new CleaningJob(new File(installationFolder,".yapbam"), new String[0], new File(Portable.getDataDirectory(),".yapbam"))); //$NON-NLS-1$ //$NON-NLS-2$
			jobs.add(new CleaningJob(new File(installationFolder,".yapbampref"), new String[0], new File(Portable.getDataDirectory(),".yapbampref"))); //$NON-NLS-1$ //$NON-NLS-2$
			for (CleaningJob job : jobs) {
				job.clean();
			}
			// It remains that damned help directory
			new File(installationFolder,"help/fr").delete(); //$NON-NLS-1$
			new File(installationFolder,"help").delete(); //$NON-NLS-1$
			// ... and, maybe sample file directory
			new File(installationFolder,"Other/samples").delete(); //$NON-NLS-1$
		} catch (Throwable e) {
			// This method must never fail.
			// If there's a problem, forget cleaning ... it's not so important.
		}
	}
	
	/** Cleans yapbam installation folder.
	 */
	static void clean() {
		// If we never ran the 0.7.2 for the first time => There's no preferences in the data directory ?
		// Danger, if we call the Preferences.getFile() method here to identify where should be the preference
		// file, it will initialize Preferences.INSTANCE and its firstRun attribute.
		// It would leads to think that it's the first Yapbam run ... but it's not if the yapbam pref file is
		// simply at the wrong place.
		if (Portable.isPortable()) {
			// If we need to perform cleaning then clean in a background thread
			new Thread(new Runnable() {
				@Override
				public void run() {
					clean (Portable.getApplicationDirectory());
				}
			}).start();
		}
	}
	
	/** A cleaning job.
	 */
	static class CleaningJob {
		File source;
		String[] checkSums;
		File destination;
		
		/** Constructor.
		 * @param source The file to move or delete.
		 * @param checkSums the possible checksum of the file. The file will be deleted only if its checksum is equal to one of these ones.
		 * @param destination the destination of the file, null to delete the file.
		 */
		CleaningJob(File source, String[] checkSums, File destination) {
			super();
			this.source = source;
			this.checkSums = checkSums;
			this.destination = destination;
		}
		
		/** Performs the cleaning.
		 */
		void clean() {
			try {
				if (source.exists() && source.canWrite() && isValidCheckSum()) {
					if (this.destination==null) {
						source.delete();
					} else {
						FileUtils.move(source, destination);
					}
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

		/** Tests whether the file has a valid checksum or not.
		 * @return true if the file has a valid checksum.
		 * @throws IOException
		 */
		private boolean isValidCheckSum() throws IOException {
			if (this.checkSums.length==0) {
				return true;
			}
			String checkSum = CheckSum.toString(CheckSum.getChecksum(source));
			for (String validCheckSum : this.checkSums) {
				if (validCheckSum.equals(checkSum)) {
					return true;
				}
			}
			return false;
		}
	}
}
