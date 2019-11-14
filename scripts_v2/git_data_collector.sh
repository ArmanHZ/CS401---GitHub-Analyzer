#!/bin/bash
# Some Git Bash commands used
# H  = Commit hash
# an = Author name
# cn = Committer name
# cd = Commit date
# s  = Subject (which dir was edited)

###
# Functions
###

currentDir=$(pwd)
cd ..
# echo "Checking whether your Repository is up to date or not."
# git pull
cd "$currentDir"
echo "Creating log files..."

function formatData()
{
	git log --name-only --pretty=format:"%H|%an|%cn|%cd|%s" > "$currentDir/FormattedData.txt" 2>&1
	printf "\n" >> "$currentDir/FormattedData.txt"
	cd "$currentDir"
	python format_data.py
}

function formatDataNoLog()
{
	git log --no-merges --name-only --pretty=format:"%H|%an|%cn|%cd|%s" > "$currentDir/FormattedDataNoMerges.txt" 2>&1
	printf "\n" >> "$currentDir/FormattedDataNoMerges.txt"
	cd "$currentDir"
	python format_data.py "-nlog"
}

function formatDataDateRestriction()
{
	parameter=$1
	git log --name-only $parameter --pretty=format:"%H|%an|%cn|%cd|%s" > "$currentDir/FormattedDataDateRestricted.txt" 2>&1
	printf "\n" >> "$currentDir/FormattedDataDateRestricted.txt"
	cd "$currentDir"
	python format_data.py $parameter
}

function statusOfFile()
{
	git log --stat --graph --decorate --no-merges --pretty=oneline --pretty='%H , %cd' > "$currentDir/stats.txt" 2>&1
	printf "\n" >> "$currentDir/stats.txt"
	cd "$currentDir"
}

function uniqueFileExtensions()
{
	git ls-files '*.*' > "$currentDir/Unique_file_extensions_temp.txt" 2>&1
	cd "$currentDir"
	python Unique_file_extensions.py
}

function filterFinalDump()
{
	arguments=$@
	cd "$currentDir"
	python Filter_final_dump.py $arguments
	exit
}

function filesChangedTogether()
{
	cd "$currentDir"
	python files_changed_together.py
	exit
}

###
# Main body of script starts here
###

if [ "$#" -eq 0 ]; then
	printf "Please enter a command.\nEx: git_data_collector.sh -h"
	exit
fi

if [[ "$@" = "-h" ]]; then
	echo "-log -> Generates commit history and commit notes."
	echo "-nlog -> Generates commit history and commit notes without merges."
	echo "--after="date" -> Shows the commits after the specified date. Date style is like Git Bash date."
	echo "--before="date" -> Shows the commits before the specified date."
	echo "-fe -> Generates a text file with unique file extensions of files in the repository."
	echo "-filter -> Filters the Final_dump.txt. It is used internally in GitHubDataVisualizer, the first argument must be -filter and must not be combined with other commands."
	exit
fi

if [[ "$#" != 0 ]] && [[ "$@" != "-h" ]]; then
	dateString=
	declare -a commands
	IFS=' '
	read -a commands <<< "$@" # parse the input for multiple commands
	cd ..
	for i in "${commands[@]}"
	do
		if [ "$i" = "-log" ]; then
			formatData
		elif [ "$i" = "-nlog" ]; then
			formatDataNoLog
		elif [[ "$i" = *after* ]] || [[ "$i" = *before* ]]; then
			dateString="${dateString} $i"
		elif [ "$i" = "-stat" ]; then
			formatData
			statusOfFile
		elif [ "$i" = "-fe" ]; then
			uniqueFileExtensions
		elif [[ "$1" = "-filter" ]] || [[ $# -gt 1 ]]; then
			filterFinalDump $@
		elif [ "$i" = "-fct" ]; then
			filesChangedTogether
		else
			printf "Command not recognized.\nList of all the commands: git_data_collector -h\n"
		fi
	done
	
	if [[ -n $dateString ]]; then
		formatDataDateRestriction $dateString
	fi

fi

exit
