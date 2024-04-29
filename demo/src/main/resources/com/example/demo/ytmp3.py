import inspect
import yt_dlp
import os
import sys

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

        print("ffmpeg binary not found.")
        return None

    def download_audio(self, video_url):
        ffmpeg_path = self.find_ffmpeg_binary()
        print("ffmpeg_path:", ffmpeg_path) 
        if not ffmpeg_path:
            print("LoL there is an Error with ffmpeg binary not found.")
            return None

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
                return None
        return os.path.join(self.desktop_path, f"{info_dict['title']}.mp3")

    def main(self):
        video_url = input("Please enter YouTube video URL: ")
        downloaded_file = self.download_audio(video_url)
        if downloaded_file:
            print("Download complete:!!!", downloaded_file)
        else:
            print("Download failed!!!!")

        ##input("Press Enter to exit...")

if __name__ == "__main__":
    downloader = YouTubeAudioDownloader()
    downloader.main()
