# Git Data Collector v2

- Place the ***scripts*** folder to the same directory where you did ***git clone***.
	- Make sure you manually use **git pull** to ensure the latest information.
- Open the Git Bash or the Kernel in the ***scripts*** folder.
- Type ***git_data_collector.sh -h*** to see currently available commands.
- Multiple commands are accepted. Ex: ***git_data_collector -log --after="date"***
- Date restriction added (currently only for stat)
	ex: ***git_data_collector --before="date" --after="date"*** where **date** is in Git Bash format.
- Date format example: --after="YY-MM-DD" -> --after="2018-05-15"
- Data collector can create **3** text files as output.
	- If you used ***-log***, **2** files will be created. **FormattedData.txt** and **final_dump.txt**
	- If you used ***-before="date"*** or ***-after="date"***, **2** files will be created. **FormattedDataDateRestricted** and **final_dump.txt**.
	
## Changelog

The previous version had an issue regarding memory. This version reads the **FormattedData**
line by line and recognizes each commit. After recognizing each commit, it immediately writes it to **final_dump.txt**.
The previous version would use file.read() to read the whole **Data** file. This caused no problems when the file was small.
We also removed **git pull** from **.sh** file. It sometimes did not work and got stuck in an infinite loop.
