from cx_Freeze import setup, Executable

setup(
    name="YouTubeAudioDownloader",
    version="1.0",
    description="Download audio from YouTube videos",
    executables=[Executable("ytmp3.py")]
)
