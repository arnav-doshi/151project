Project title
RhythmRipper

Team #, team members 
Team 13
Arnav Doshi
Owen Hall
Sidharth Krishnaswamy

Team members working on the proposal 
Arnav Doshi
Owen Hall
Sidharth Krishnaswamy

State the problem/issue to resolve 
Youtube audios are not downloadable without Youtube Premium. We decided to create an application that lets you download these audio files for free. 

If applicable, briefly survey previous works if any (include references) 
An app that has a similar idea as us: https://ytmp3.cc/e87c/
We can improve on the UI
Less Ads 
Make it look less sketchy/clean up any text

If applicable, describe assumptions / operating environments / intended usage 
Using IntelliJ (Java), Git, Youtube, 
We plan to make it a native desktop application, utilizing YouTube’s Java API. Users will download the application onto their computers, input YouTube links, and download/play the converted audio files directly onto their local machines.

High-level description of your solution which may include (but is not limited to), your plan and approach.  Be as specific as possible. 
Accept a YouTube link as user input (error handling for input)
Utilizing either a library or implementing our own code to download the Youtube video
Converting downloaded video to .mp3 format (using JAVE or FFmpeg Java libraries)
The application would integrate an audio player component that can handle streaming audio. 
This player would have controls for play, pause, volume adjustment, etc.
Documentation required with JavaDoc/Unit testing

Functionality: describe how your solution tackles the issues 
Our solution provides a direct answer to the issue by simply asking the user for the YouTube link and providing a seamless way for them to download and play the audio that is desired. 

Operations: List operations for each intended user (in list format).  Be precise and specific. 

Users
Input YouTube links in input field
Click on a “convert” button to start the conversion process
Click on a link/button to play the mp3 file on our site
Download the mp3 player on their own computer

Admins
Configure the application (Setup file storage location, error handling, UI)
Monitor app (Any errors during conversion or playback, resource usage on the site)
Maintain app (Update any dependencies, fix any user-reported bugs, increase performance)



