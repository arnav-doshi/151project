import os
import yt_dlp

class YouTubeAudioDownloader:
    def __init__(self):
        self.desktop_path = os.path.join(os.path.expanduser("~"), "Desktop")

    def download_audio(self, video_url):
        with yt_dlp.YoutubeDL() as ydl:
            info_dict = ydl.extract_info(video_url, download=False)
            video_title = info_dict.get('title', 'video')

        output_path = os.path.join(self.desktop_path, f"{video_title}")

        ydl_opts = {
            'format': 'bestaudio/best',
            'postprocessors': [{
                'key': 'FFmpegExtractAudio',
                'preferredcodec': 'mp3',
                'preferredquality': '192',
            }],
            'outtmpl': output_path, 
        }

        with yt_dlp.YoutubeDL(ydl_opts) as ydl:
            try:
                info_dict = ydl.extract_info(video_url, download=True)
            except yt_dlp.utils.DownloadError as e:
                print("Error downloading the video:", e)
                return None
        return output_path

    def main(self):
        video_url = input("Please enter YouTube video URL: ")
        downloaded_file = self.download_audio(video_url)
        if downloaded_file:
            print("Download complete:", downloaded_file)
        else:
            print("Download failed.")

if __name__ == "__main__":
    downloader = YouTubeAudioDownloader()
    downloader.main()
