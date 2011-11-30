package net.yapbam.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LnkParser {

  public LnkParser(File f) throws IOException {
      parse(f);
  }

  private String real_file;
  private boolean isDirectory;
  private boolean isLocal;

  public boolean isDirectory() {
      return isDirectory;
  }

  public String getRealFilename() {
      return real_file;
  }

  private void parse(File f) throws IOException {
      // read the entire file into a byte buffer
      FileInputStream fin = new FileInputStream(f);
      try {
	      parse(fin);
      } finally {
      	fin.close();
      }
  }
  
  private void parse(InputStream stream) throws IOException {
  	parse(new SequentialByteReader(stream));
  }

  private void parse(SequentialByteReader reader) throws IOException {
      // get the flags byte
      byte flags = reader.getByte(0x14);

      // get the file attributes byte
      final int file_atts_offset = 0x18;
      byte file_atts = reader.getByte(file_atts_offset);
      byte is_dir_mask = (byte)0x10;
      if ((file_atts & is_dir_mask) > 0) {
          isDirectory = true;
      } else {
          isDirectory = false;
      }

      // if the shell settings are present, skip them
      final int shell_offset = 0x4c;
      final byte has_shell_mask = (byte)0x01;
      int shell_len = 0;
      if ((flags & has_shell_mask) > 0) {
          // the plus 2 accounts for the length marker itself
          shell_len = bytes2short(reader, shell_offset) + 2;
      }

      // get to the file settings
      int file_start = 0x4c + shell_len;

      final int file_location_info_flag_offset_offset = 0x08;
      int file_location_info_flag = reader.getByte(file_start + file_location_info_flag_offset_offset);
      isLocal = (file_location_info_flag & 2) == 0;
      // get the local volume and local system values
      //final int localVolumeTable_offset_offset = 0x0C;
      final int basename_offset_offset = 0x10;
      final int networkVolumeTable_offset_offset = 0x14;
      final int finalname_offset_offset = 0x18;
      int finalname_offset = reader.getByte(file_start + finalname_offset_offset) + file_start;
      String finalname = getNullDelimitedString(reader, finalname_offset);
      if (isLocal) {
          int basename_offset = reader.getByte(file_start + basename_offset_offset) + file_start;
          String basename = getNullDelimitedString(reader, basename_offset);
          real_file = basename + finalname;
      } else {
          int networkVolumeTable_offset = reader.getByte(file_start + networkVolumeTable_offset_offset) + file_start;
          int shareName_offset_offset = 0x08;
          int shareName_offset = reader.getByte(networkVolumeTable_offset + shareName_offset_offset) + networkVolumeTable_offset;
          String shareName = getNullDelimitedString(reader, shareName_offset);
          real_file = shareName + "\\" + finalname;
      }
  }

  private static String getNullDelimitedString(SequentialByteReader reader, int off) throws IOException {
  	StringBuilder buf = new StringBuilder();
      // count bytes until the null character (0)
      while (true) {
          if (reader.getByte(off) == 0) {
          		buf.append(reader.getByte(off));
              break;
          }
      }
      return buf.toString();
  }

  /*
   * convert two bytes into a short note, this is little endian because it's for an Intel only OS.
   */
  private static int bytes2short(SequentialByteReader reader, int off) throws IOException {
  	byte bLow = reader.getByte(off);
  	byte bHigh = reader.getByte(off+1);
    int low = (bLow<0 ? bLow+256 : bLow);
    int high = (bHigh<0 ? bHigh+256 : bHigh)<<8;
    return 0 | low | high;
}

  /**
   * Returns the value of the instance variable 'isLocal'.
   *
   * @return Returns the isLocal.
   */
  public boolean isLocal() {
      return isLocal;
  }
}