package com.studio.ffmpeg;

import java.util.LinkedList;
import java.util.List;
import android.os.Environment;

public class FfmpegJob {

	String st = Environment.getExternalStorageDirectory().getPath() + "/"
			+ "Android_Studio/";
	String trimst = Environment.getExternalStorageDirectory().getPath() + "/"
			+ "Android_Studio/View_Trimmed_Files/";
	String converted = Environment.getExternalStorageDirectory().getPath()
			+ "/" + "Android_Studio/View_Converted_files/";
	String merged = Environment.getExternalStorageDirectory().getPath() + "/"
			+ "Android_Studio/View_Merged_Videos/";
	String imagest = Environment.getExternalStorageDirectory().getPath() + "/"
			+ "Android_Studio/View_Extracted_Images/";
	String extract = Environment.getExternalStorageDirectory().getPath() + "/"
			+ "Android_Studio/View_Extracted_Files/";

	public String imageformat, start, framerate, duration, arr;
	public String imageframesize;
	public int starttime;
	public int trimduration;
	public String inputPath;
	public String outputPath;
	public String outputimg;
	String filename;
	String trimfilename;
	String picformat;
	String fileformat;
	String file_path;
	int flag;
	String audiofilepath, videofilepath, destfilename;
	private final String mFfmpegPath;

	public FfmpegJob(String ffmpegPath, String start, String framerate,
			String duration, String arr, String filename, String picformat,
			String file_path) {
		mFfmpegPath = ffmpegPath;
		this.start = start;
		this.framerate = framerate;
		this.duration = duration;
		this.arr = arr;
		this.filename = filename;
		this.picformat = picformat;
		this.file_path = file_path;
	}

	public FfmpegJob(String ffmpegPath, String filename, String fileformat) {
		mFfmpegPath = ffmpegPath;
		this.filename = filename;
		this.fileformat = fileformat;

	}

	public FfmpegJob(String ffmpegPath, String trimfilename,
			String destfilename, int starttime, int trimduration) {
		mFfmpegPath = ffmpegPath;
		this.trimfilename = destfilename;
		this.file_path = trimfilename;
		this.starttime = starttime;
		this.trimduration = trimduration;

	}

	public FfmpegJob(String ffmpegPath, String filename, String fileformat,
			String file_path) {
		mFfmpegPath = ffmpegPath;
		this.filename = filename;
		this.fileformat = fileformat;
		this.file_path = file_path;
	}

	public FfmpegJob(String ffmpegPath, String audiofilepath,
			String videofilepath, String destfilename, String fileformat,
			int flag) {
		mFfmpegPath = ffmpegPath;
		this.audiofilepath = audiofilepath;
		this.videofilepath = videofilepath;
		this.destfilename = destfilename;
		this.fileformat = fileformat;
		this.flag = flag;
	}

	public FfmpegJob(String ffmpegPath) {
		mFfmpegPath = ffmpegPath;
	}

	public String getmFfmpegPath() {
		return mFfmpegPath;
	}

	public ProcessRunnable create1() {

		// if (inputPath == null || outputPath == null) {
		// throw new IllegalStateException("Need An input and ouput file");
		// }
		final List<String> cmd4 = new LinkedList<String>();
		// ffmpeg -i inputfile.avi -r 1 -f image2 image-%3d.jpeg
		cmd4.add(mFfmpegPath);
		cmd4.add("-y");
		cmd4.add("-ss");
		cmd4.add(start);
		cmd4.add("-i");
		cmd4.add(file_path);
		cmd4.add("-an");
		// cmd4.add("00:00:12");
		cmd4.add("-r");
		// cmd4.add("1");
		cmd4.add(framerate);
		cmd4.add("-s");
		// cmd4.add("4cif");
		cmd4.add(arr);
		cmd4.add("-t");
		// cmd4.add("4");
		cmd4.add(duration);
		cmd4.add(imagest + filename + "-%3d" + picformat);

		ProcessBuilder pb = new ProcessBuilder(cmd4);
		return new ProcessRunnable(pb);

	}

	// function to create a command of Audio Extraction from Video.
	public ProcessRunnable create2() {
		// if (inputPath == null || outputPath == null) {
		// throw new IllegalStateException(
		// "Need an input and output filepath!");
		// }
		final List<String> cmd = new LinkedList<String>();
		cmd.add(mFfmpegPath);
		cmd.add("-y");
		cmd.add("-i");
		cmd.add(file_path);
		cmd.add("-vn");
		if (fileformat.equals(".mp3")) {
			cmd.add("-ar");
			cmd.add("44100");
			cmd.add("-ab");
			cmd.add("320k");
			// cmd.add("-acodec");
			// cmd.add("copy");
			// cmd.add("-vcodec");
			// cmd.add("copy");
			cmd.add(extract + com.studio.Constants.WAVE_FILE);
		} else if (fileformat.equalsIgnoreCase(".wav")) {
			cmd.add("-ar");
			cmd.add("22050");
			cmd.add("-ab");
			cmd.add("128k");
			cmd.add(extract + filename + fileformat);
		} else {
			cmd.add("-acodec");
			cmd.add("copy");
			cmd.add(extract + filename + fileformat);
		}
		ProcessBuilder pb = new ProcessBuilder(cmd);
		return new ProcessRunnable(pb);
	}

	public ProcessRunnable create4() {
		// if (inputPath == null || outputPath == null) {
		// throw new IllegalStateException("Need An input and ouput file"); }

		final List<String> cmd2 = new LinkedList<String>();
		cmd2.add(mFfmpegPath);
		cmd2.add("-y");
		cmd2.add("-i");
		cmd2.add(file_path);
		cmd2.add("-an");
		cmd2.add(extract + filename + fileformat);
		ProcessBuilder pb = new ProcessBuilder(cmd2);
		return new ProcessRunnable(pb);

	}

	public ProcessRunnable create3() {
		// if (inputPath == null || outputPath == null) {
		// throw new IllegalStateException("Need An input and ouput file");
		// }
		final List<String> cmd1 = new LinkedList<String>();
		cmd1.add(mFfmpegPath);
		cmd1.add("-y");
		cmd1.add("-i");
		cmd1.add(file_path);
		cmd1.add(converted + filename + fileformat);
		// ffmpeg -i input.mkv -map 0:1 -map 0:2 -c copy output.mkv
		// ffmpeg -i source_video.avi -vn -ar 44100 -ac 2 -ab 192 -f mp3
		// sound.mp3

		ProcessBuilder pb = new ProcessBuilder(cmd1);
		return new ProcessRunnable(pb);

	}

	public ProcessRunnable trimvideos() {
		// if (inputPath == null || outputPath == null) {
		// throw new IllegalStateException();
		// }
		final List<String> cmd10 = new LinkedList<String>();
		// ffmpeg -i inputfile.avi -croptop 88 -cropbottom 88 -cropleft 360
		// -cropright 360 outputfile.avi

		// ffmpeg -ss 2 -t 120 -vcodec copy -acodec copy -i input.file
		// output.file

		cmd10.add(mFfmpegPath);
		cmd10.add("-y");
		cmd10.add("-ss");
		// cmd10.add("00:00:12");
		cmd10.add(String.valueOf(starttime));
		cmd10.add("-t");
		// cmd10.add("00:00:45");
		cmd10.add(String.valueOf(trimduration));
		cmd10.add("-i");
		// cmd10.add(st+"bairya.mp4");
		cmd10.add(file_path);
		// cmd10.add("-vn");
		cmd10.add("-acodec");
		cmd10.add("copy");
		cmd10.add("-vcodec");
		cmd10.add("copy");
		// cmd10.add(st+"cutyyyyyyy.mp4");
		cmd10.add(trimst + trimfilename);
		// cmd10.add(trimst+"file.m4a");
		ProcessBuilder pb = new ProcessBuilder(cmd10);
		return new ProcessRunnable(pb);
	}

	public ProcessRunnable addaudio() {
		// if (inputPath == null || outputPath == null) {
		// throw new IllegalStateException();
		// }
		final List<String> cmd10 = new LinkedList<String>();
		// ffmpeg -i inputfile.avi -croptop 88 -cropbottom 88 -cropleft 360
		// -cropright 360 outputfile.avi
		// ffmpeg -ss 2 -t 120 -vcodec copy -acodec copy -i input.file
		// output.file

		cmd10.add(mFfmpegPath);
		cmd10.add("-y");

		cmd10.add("-i");
		cmd10.add(videofilepath);
		cmd10.add("-i");
		cmd10.add(audiofilepath);
		cmd10.add("-map");
		cmd10.add("0:0");
		cmd10.add("-map");
		cmd10.add("1:1");
		cmd10.add("-shortest");
		// cmd10.add("mnt/sdcard/pop.m4a");
		cmd10.add("-vcodec");
		cmd10.add("copy");
		cmd10.add("-acodec");
		cmd10.add("copy");

		// cmd10.add(st+"cutyyyy.mp4");
		cmd10.add(merged + destfilename + fileformat);
		ProcessBuilder pb = new ProcessBuilder(cmd10);
		return new ProcessRunnable(pb);
	}

	public ProcessRunnable create7() {
		// if (inputPath == null || outputPath == null) {
		// throw new IllegalStateException();
		// }
		final List<String> cmd7 = new LinkedList<String>();
		// ffmpeg -i inputfile.avi -croptop 88 -cropbottom 88 -cropleft 360
		// -cropright 360 outputfile.avi

		cmd7.add(mFfmpegPath);
		// cmd7.add("-y");
		// cmd7.add("-i");
		// cmd7.add(inputPath);
		// cmd7.add("-croptop");
		// cmd7.add("50");
		// cmd7.add("-cropbottom");
		// cmd7.add("50");
		// cmd7.add("-cropleft");
		// cmd7.add("50");
		// cmd7.add("cropright");
		// cmd7.add("50");
		// cmd7.add(outputPath);
		cmd7.add("-y");

		// cmd7.add("-loop_input");
		// cmd7.add("1");

		cmd7.add("-f");
		cmd7.add("image2");

		cmd7.add("-i");
		cmd7.add(Environment.getExternalStorageDirectory().getPath() + "/"
				+ "screenshot.png");
		// cmd7.add("/mnt/sdcard/000124.jpg");
		cmd7.add("-i");
		// cmd7.add("/mnt/sdcard/audio.wav");
		cmd7.add(Environment.getExternalStorageDirectory().getPath() + "/"
				+ "Naina.mp3");
		cmd7.add("-map");
		cmd7.add("0.0");
		cmd7.add("-map");
		cmd7.add("1.0");
		cmd7.add("-acodec");
		cmd7.add("copy");
		cmd7.add("-vcodec");
		cmd7.add("mjpeg");

		cmd7.add(st + "video.mp4");
		// cmd7.add("mnt/sdcard/res.mp4");

		ProcessBuilder pb = new ProcessBuilder(cmd7);
		return new ProcessRunnable(pb);
	}

	public ProcessRunnable create5() {
		// if (inputPath == null || outputPath == null) {
		// throw new IllegalStateException();
		// }
		// ffmpeg -f oss -i /dev/dsp audio.mp3
		final List<String> cmd4 = new LinkedList<String>();
		// ffmpeg -i video.mp4 -i audio.wav \
		// -c:v copy -c:a aac -strict experimental \
		// -map 0:v:0 -map 1:a:0 output.mp4

		// ffmpeg -i bairya.mp4 -vf vflip -acodec copy bai.mp4
		// ffmpeg -i largefile.mp4 -t 00:50:00 -c copy smallfile1.mp4 -ss
		// 00:50:00 -c copy smallfile2.mp4

		// Trim Audio Functionality

		cmd4.add(mFfmpegPath);
		cmd4.add("-y");

		cmd4.add("-i");
		cmd4.add(st + "bairya.mp4");
		cmd4.add("-vf");
		cmd4.add("vflip=");
		cmd4.add("1");
		cmd4.add("-sameq");
		// cmd4.add("-vcodec");
		// cmd4.add("copy");
		cmd4.add(st + "flipexple.mp4");

		// // make video from images functionality
		// cmd4.add(mFfmpegPath);
		// cmd4.add("-y");
		// cmd4.add("-f");
		// cmd4.add("image2");
		// cmd4.add("-r");
		// cmd4.add("1");
		// // cmd4.add("-pattern_type glob");
		// // cmd4.add("-i");
		// cmd4.add(imagest+ "bnm-%03d.jpeg");
		// cmd4.add("-r");
		// cmd4.add("12");
		// cmd4.add("-s");
		// cmd4.add("xga");
		// cmd4.add(st + "video1.mp4");

		// ffmpeg -f image2 -i image%d.jpg video.mpg // sample2
		// cmd4.add("mFfmpegPath");
		// cmd4.add("-y");
		// cmd4.add("image2");
		// cmd4.add("-i");
		// cmd4.add("download%d.jpg");
		// cmd4.add("mnt/sdcatd/video.mpg");

		ProcessBuilder pb = new ProcessBuilder(cmd4);
		return new ProcessRunnable(pb);
	}

	public class FFMPEGArg {
		String key;
		String value;

		public static final String ARG_VIDEOCODEC = "-vcodec";
		public static final String ARG_AUDIOCODEC = "-acodec";

		public static final String ARG_VIDEOBITSTREAMFILTER = "-vbsf";
		public static final String ARG_AUDIOBITSTREAMFILTER = "-absf";

		public static final String ARG_VIDEOFILTER = "-vf";
		public static final String ARG_AUDIOFILTER = "-af";

		public static final String ARG_VERBOSITY = "-v";
		public static final String ARG_FILE_INPUT = "-i";
		public static final String ARG_SIZE = "-s";
		public static final String ARG_FRAMERATE = "-r";
		public static final String ARG_FORMAT = "-f";
		public static final String ARG_BITRATE_VIDEO = "-b:v";

		public static final String ARG_BITRATE_AUDIO = "-b:a";
		public static final String ARG_CHANNELS_AUDIO = "-ac";
		public static final String ARG_FREQ_AUDIO = "-ar";
		public static final String ARG_VOLUME_AUDIO = "-vol";

		public static final String ARG_STARTTIME = "-ss";
		public static final String ARG_DURATION = "-t";

		public static final String ARG_DISABLE_AUDIO = "-an";
		public static final String ARG_DISABLE_VIDEO = "-vn";
	}
}
