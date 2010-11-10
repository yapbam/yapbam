package net.yapbam.gui;

import java.io.File;
import java.io.IOException;

import net.yapbam.util.CheckSum;
import net.yapbam.util.FileUtils;
import net.yapbam.util.Portable;

/** This class is in charge of removing obsoletes files from previous Yapbam versions. 
 */
class FolderCleaner {
	private static CleaningJob[] JOBS;
	
	private FolderCleaner(){}
	
	static void clean() {
		try {
			if (JOBS==null) {
				File main = Portable.getLaunchDirectory();
				JOBS = new CleaningJob[] {
					new CleaningJob(new File(main,"YapbamDataSample_en.xml"), "305c894fe05c386405592e29d01ce0a7", null),
					new CleaningJob(new File(main,"YapbamDataSample_fr.xml"), "7a3e76824e74672d4a3941c4ab219720", null),
					new CleaningJob(new File(main,"Installation instructions.html"), "-48f4d394d5de7fab2d9199d56f54b83f", null),
					new CleaningJob(new File(main,"contributors.html"), "511612421c8c0a2629d5b77a3dd1b4cc", null),
					new CleaningJob(new File(main,"license.html"), "-4da76f5b973bd32b5a837cc958195fdf", null),
					new CleaningJob(new File(main,"Release notes.html"), new String[]{"12301e87f607744428ccbdd3016f9a70"}, null), //TODO release notes of older versions
					new CleaningJob(new File(main,"help/import.html"), "34f3e9d273c20de561050412983d0182", null),
					new CleaningJob(new File(main,"help/regexp.html"), "4a5a2c4935fc07fb6861026240379292", null),
					new CleaningJob(new File(main,"help/fr/import.html"), "-5cfb2231d119e70cb2ab3a48c6a43e92", null),
					new CleaningJob(new File(main,"help/fr/regexp.html"), "69b6bc3e04fc55aa78b88d477c734ae8", null),
					new CleaningJob(new File(main,"yapbam.jar"), new String[0], null),
					new CleaningJob(new File(main,".yapbam"), new String[0], new File(Portable.getDataDirectory(),".yapbam")),
					new CleaningJob(new File(main,".yapbampref"), new String[0], new File(Portable.getDataDirectory(),".yapbampref"))
				};
			}
			if (needToClean()) { // If we need to perform cleaning ?
				for (CleaningJob job : JOBS) {
					job.clean();
				}
			}
		} catch (Throwable e) {
			// This method must never fail.
			// If there's a problem, forget cleaning ... it's not so important.
		}
	}
	
	private static boolean needToClean() {
		// Yes if we never ran the 0.7.2 => There's no preferences in the data directory ?
		//TODO
		return true;
	}
	
	static class CleaningJob {
		File source;
		String[] checkSums;
		File destination;
		
		CleaningJob(File source, String[] checkSums, File destination) {
			super();
			this.source = source;
			this.checkSums = checkSums;
			this.destination = destination;
		}

		CleaningJob(File source, String checkSum, File destination) {
			this(source, new String[]{checkSum}, destination);
		}
		
		void clean() {
			try {
				if (source.exists() && source.canWrite()) {
					if (isValidCheckSum()) {
						if (this.destination==null) {
							source.delete();
						} else {
							FileUtils.move(source, destination);
						}
					}
				}
			} catch (Throwable e) {
				System.err.println ("Error on file "+source);
				e.printStackTrace();
			}
		}

		private boolean isValidCheckSum() throws IOException {
			if (this.checkSums.length==0) return true;
			String checkSum = CheckSum.toString(CheckSum.createChecksum(source));
			for (String validCheckSum : this.checkSums) {
				if (validCheckSum.equals(checkSum)) return true;
			}
			System.out.println (source+" -> invalid check sum : "+checkSum); //TODO
			return false;
		}
	}
}
