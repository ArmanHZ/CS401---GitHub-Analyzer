## How to use the "git_data_collector.sh".

1) Place the ***scripts*** folder to the same directory where you did ***git clone***.
2) Open the Git Bash or the Kernel in the ***scripts*** folder.
3) Type ***git_data_collector.sh -h*** to see currently available commands.
4) Multiple commands are accepted. Ex: ***git_data_collector -log --after="date"***
5) Date restriction added (currently only for stat)
	ex: ***git_data_collector --before="date" --after="date"*** where **date** is in Git Bash format.
6) Date format example: --after="YY-MM-DD" -> --after="2018-05-15"
