
import os
import sys
import yt_dlp

class YouTubeAudioDownloader:
    def __init__(self):
        self.desktop_path = os.path.join(os.path.expanduser("~"), "Desktop")

    def find_script_directory(self):
        script_path = sys.argv[0]
        script_dir = os.path.dirname(os.path.abspath(script_path))
        return script_dir

    def find_ffmpeg_binary(self):
        script_dir = self.find_script_directory()
        ffmpeg_dir = os.path.join(script_dir, "ffmpeg")
        ffmpeg_path = os.path.join(ffmpeg_dir, "ffmpeg.exe")

        print("Searching for ffmpeg binary in:", ffmpeg_path)

        if os.path.exists(ffmpeg_path):
            return ffmpeg_path
        else:
            print("ffmpeg binary not found.")
            return None

    def download_audio_and_video(self, video_url):
        ffmpeg_path = self.find_ffmpeg_binary()
        print("ffmpeg_path:", ffmpeg_path) 
        if not ffmpeg_path:
            print("Error. FFMPEG binary not found.")
            return None, None

        os.environ['PATH'] = os.environ['PATH'] + os.pathsep + os.path.dirname(ffmpeg_path)

        ydl_opts = {
            'format': 'bestaudio/best',
            'postprocessors': [{
                'key': 'FFmpegExtractAudio',
                'preferredcodec': 'mp3',
                'preferredquality': '192',
            }],
            'outtmpl': os.path.join(self.desktop_path, '%(title)s.%(ext)s'),
        }

        with yt_dlp.YoutubeDL(ydl_opts) as ydl:
            try:
                info_dict = ydl.extract_info(video_url, download=True)
            except yt_dlp.utils.DownloadError as e:
                print("Error with downloading the video:", e)
                return None, None

        audio_file = os.path.join(self.desktop_path, f"{info_dict['title']}.mp3")

        ydl_opts_video = {
            'format': 'bestvideo[ext=mp4]+bestaudio[ext=m4a]/mp4',
            'outtmpl': os.path.join(self.desktop_path, '%(title)s.%(ext)s'),
        }

        with yt_dlp.YoutubeDL(ydl_opts_video) as ydl_video:
            try:
                info_dict_video = ydl_video.extract_info(video_url, download=True)
            except yt_dlp.utils.DownloadError as e:
                print("Error with downloading the video:", e)
                return audio_file, None

        video_file = os.path.join(self.desktop_path, f"{info_dict_video['title']}.mp4")

        return audio_file, video_file

    def main(self):
        video_url = input("Please enter YouTube video URL: ")
        audio_file, video_file = self.download_audio_and_video(video_url)

        if audio_file and video_file:
            print("Download complete:!!!")
            print("Audio File:", audio_file)
            print("Download complete for video!!!")
            print("Video File:", video_file)
        else:
            print("Download failed!!!!")

if __name__ == "__main__":
    downloader = YouTubeAudioDownloader()
    downloader.main()




