package com.app.support.file;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Kent on 2017/1/23.
 */
public class FlatFileWriter {

	private final String TAG = this.getFileName();
	private BufferedWriter br;
	private File file;
	private String filePath;
	private String fileName;
	private int recordCount = 0;
	private int commitInterval = 1000;
	private boolean overrideAutomatically = false;
	
	public void open() {
		if(StringUtils.isBlank(filePath))
			throw new IllegalArgumentException("file path not asigned.");
		
		File path = new File(filePath);
		if(!path.exists()) {
			path.mkdirs();
		}
		
		if(!path.canWrite())
			throw new IllegalStateException("access denied:" + filePath);
		
		file = new File(filePath + SystemUtils.FILE_SEPARATOR + fileName);
		if(file.exists()) {
			if(overrideAutomatically) {
                //noinspection HardCodedStringLiteral
//                Log.w(TAG, "file exists, override automatically:" + file.getName());
			} else {
				throw new RuntimeException("file " + file.getName() + " already exists.");
			}
		}
		
		try {
			br = new BufferedWriter(new FileWriter(file));
		} catch (IOException e) {
//			Log.e(TAG, e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	public void close() {
		if(br == null)
			return;
		
		try {
			br.flush();
			br.close();
		} catch (IOException e) {
//			Log.e(TAG, e.getMessage(), e);
		}
		
	}
	
	public void writeLine(String record) {
		try {
			br.write(record);
			br.newLine();
			
			recordCount++;
			if(commitInterval != 0 && recordCount >= commitInterval) {
				br.flush();
				recordCount = 0;
			}
		} catch (IOException e) {
			if(br != null) {
				try {
					br.flush();
				} catch (IOException e1) {
                    //noinspection HardCodedStringLiteral
//                    Log.e(TAG, "flush IO buffer error:", e1);
				}
			}

//			Log.e(TAG, e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	public File getGeneratedFile() {
		return file;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		if(StringUtils.isBlank(filePath))
			throw new IllegalArgumentException("file path should not be null or blank.");
		
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		if(StringUtils.isBlank(fileName))
			throw new IllegalArgumentException("file name should not be null or blank.");
		
		this.fileName = PatternConverter.convert(fileName);
	}

	public int getCommitInterval() {
		return commitInterval;
	}

	public void setCommitInterval(int commitInterval) {
		if(commitInterval < 0)
			throw new IllegalArgumentException("commit interval must great than 0");
		
		this.commitInterval = commitInterval;
	}

	public boolean isOverrideAutomatically() {
		return overrideAutomatically;
	}

	public void setOverrideAutomatically(boolean overrideAutomatically) {
		this.overrideAutomatically = overrideAutomatically;
	}

}
