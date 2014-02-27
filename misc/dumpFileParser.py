#!/usr/bin/python

__author__ = 'Peilong'

import numpy as np
import matplotlib.pyplot as plt
from pprint import pprint
import string
import re
import json
import shutil
import os
import sys
import getopt

# Declare Global Variables
view_taskTimeline   = []
view_deserTime      = []
view_exeTime        = []
view_serTime        = []
view_statistic      = []
view_schedulerDelay = []

# Append the first JSON array
view_deserTime.append(['Task Info', 'Duration in ms'])
view_exeTime.append(['Task Info', 'Duration in ms'])
view_serTime.append(['Task Info', 'Duration in ms'])
view_schedulerDelay.append(['Task Info', 'Duration in ms'])

# Statistic Metrics Initialization
endTimeMax     = 0
startTimeMin   = sys.maxint
emptyTask      = 0
deserTimeMax   = 0
deserTimeMin   = sys.maxint
deserTimeTotal = 0
exeTimeMax     = 0
exeTimeMin     = sys.maxint
exeTimeTotal   = 0
serTimeMax     = 0
serTimeMin     = sys.maxint
serTimeTotal   = 0
schedulerDelayTotal = 0
schedulerDelayMax   = 0
schedulerDelayMin   = sys.maxint

# Regular Expressing Matching Function
def regexMatch (fileString):
	global emptyTask
	global endTimeMax
	global startTimeMin
	global deserTimeMax
	global deserTimeMin
	global deserTimeTotal
	global exeTimeMax
	global exeTimeMin
	global exeTimeTotal
	global serTimeMax
	global serTimeMin
	global serTimeTotal
	global schedulerDelayTotal
	global schedulerDelayMax
	global schedulerDelayMin
	for tid in xrange (0, len(fileString)/6):
		startPattern = re.compile(r'^(\d+)\tStarting TID '+str(tid)+' on (.*)')
		endPattern = re.compile(r'^(\d+)\tFinished TID '+str(tid)+' on (.*)')
		deserPattern = re.compile(r'^(\d+)\t(\d+)\tDeserialization of TID '+str(tid)+': (\d+) ms on (.*)' )
		exePattern = re.compile(r'^(\d+)\t(\d+)\tExecution of TID '+str(tid)+': (\d+) ms on (.*)' )
		serPattern = re.compile(r'^(\d+)\t(\d+)\tSerialization of TID '+str(tid)+': (\d+) ms on (.*)')
		for line in fileString:
			startMatch = startPattern.match(line) 
			if startMatch:
				startTime = startMatch.group(1)
				host = startMatch.group(2)
				
			endMatch = endPattern.match(line) 
			if endMatch:
				endTime = endMatch.group(1)
				
			deserMatch = deserPattern.match(line)
			if deserMatch:
				deserStart = deserMatch.group(1)
				deserEnd   = deserMatch.group(2)
				deserTime  = deserMatch.group(3)
				deserTimeTotal = deserTimeTotal + int(deserTime)
				if (startTimeMin > int(deserStart)):
					startTimeMin = int(deserStart)
				if (deserTimeMax < int(deserTime)):
					deserTimeMax = int(deserTime)
				if (deserTimeMin > int(deserTime)):
					deserTimeMin = int(deserTime)
				
			exeMatch = exePattern.match(line)
			if exeMatch:
				exeStart = exeMatch.group(1)
				exeEnd   = exeMatch.group(2)
				exeTime  = exeMatch.group(3)
				exeTimeTotal = exeTimeTotal + int(exeTime)
				if (int(exeTime) < 200):
					emptyTask = emptyTask + 1
				if (exeTimeMax < int(exeTime)):
					exeTimeMax = int(exeTime)
				if (exeTimeMin > int(exeTime)):
					exeTimeMin = int(exeTime)
					
			serMatch = serPattern.match(line)
			if serMatch:
				serStart = serMatch.group(1)
				serEnd   = serMatch.group(2)
				serTime  = serMatch.group(3)
				serTimeTotal = serTimeTotal + int(serTime)
				if (endTimeMax < int(serEnd)):
					endTimeMax = int(serEnd)
				if (serTimeMax < int(serTime)):
					serTimeMax = int(serTime)
				if (serTimeMin > int(serTime)):
					serTimeMin = int(serTime)
		
		schedulerDelay = int(endTime ) - int(startTime) - int(exeTime)
		schedulerDelayTotal = schedulerDelayTotal + schedulerDelay
		if (schedulerDelayMax < int(schedulerDelay)):
			schedulerDelayMax = int(schedulerDelay)
		if (schedulerDelayMin > int(schedulerDelay)):
			schedulerDelayMin = int(schedulerDelay)
		
		list_deser = [str(tid), 'De', deserStart, deserEnd]
		list_exe   = [str(tid), 'Exe', exeStart, exeEnd]	
		list_ser   = [str(tid), 'Ser', serStart, serEnd]	
		list_schedulerDelay = ['Task '+str(tid)+' @ '+host, schedulerDelay]
		list_deserTime = ['Task '+str(tid)+' @ '+host, int(deserTime)]
		list_exeTime   = ['Task '+str(tid)+' @ '+host, int(exeTime)]
		list_serTime   = ['Task '+str(tid)+' @ '+host, int(serTime)]
	
		if (int(deserEnd) > int(deserStart)):
			view_taskTimeline.append(list_deser)
		if (int(exeEnd) > int(exeStart)):
			view_taskTimeline.append(list_exe)
		if (int (serEnd) > int(serStart)):
			view_taskTimeline.append(list_ser)
		
		view_schedulerDelay.append(list_schedulerDelay)
		view_deserTime.append(list_deserTime)
		view_exeTime.append(list_exeTime)
		view_serTime.append(list_serTime)

# Input Argument Parser
def argParser():
	UsageInfo = "Usage: %s inputFile\n"
	if len(sys.argv) != 2:
		print(UsageInfo % sys.argv[0])
		sys.exit(2)
	else:
		arg = sys.argv[1]
		if os.path.exists(arg):
			return os.path.basename(arg)
		else:
			print(UsageInfo % sys.argv[0])
			sys.exit(2)

# Print Header Information
def printHeader():
	print("+"*60)
	print("+           SPARKLING INPUT DATA GENERATOR")
	print("+ AUTHOER: PEILONG LI")
	print("+ ORGNISATION: UMASS LOWELL CANS LAB")
	print("+"*60+"\n")
	
# Main Function Entry
def main():
	# Print App Header Message
	printHeader()
	
	# Get fileString from a file
	curr_dir = os.getcwd()
	inputFileName = argParser();
	dumpFile = open(curr_dir+'/'+inputFileName,'r')
	fileString = dumpFile.readlines()

	# Regex Match
	print("-"*60)
	print("- Starting Regular Expression Matching...")
	print("- It may take a while depending on the length of your input file")
	regexMatch(fileString)
	print("- Regular Expression Matching Finished!")
	print("-"*60+"\n")
	
	# Calculate Metrics Value
	emptyTaskPercentage   = 100.00 * emptyTask/float(len(fileString)/6)
	schedulerDelayAverage = schedulerDelayTotal/float(len(fileString)/6)
	deserTimeAverage      = deserTimeTotal/float(len(fileString)/6)
	exeTimeAverage        = exeTimeTotal/float(len(fileString)/6)
	serTimeAverage        = serTimeTotal/float(len(fileString)/6)
	overallDuration       = (endTimeMax - startTimeMin)/1000.0/60.0
	
	# Combine Metrics Values into JSON Data
	statisticData = {'emptyTaskPercentage':emptyTaskPercentage,\
		'schedulerDelayAverage':schedulerDelayAverage,\
		'schedulerDelayMax':float(schedulerDelayMax),\
		'schedulerDelayMin':float(schedulerDelayMin),\
		'deserTimeAverage':deserTimeAverage,\
		'deserTimeMax':float(deserTimeMax),\
		'deserTimeMin':float(deserTimeMin),\
		'exeTimeAverage':exeTimeAverage,\
		'exeTimeMax':float(exeTimeMax),\
		'exeTimeMin':float(exeTimeMin),\
		'serTimeAverage':serTimeAverage,\
		'serTimeMax':float(serTimeMax),\
		'serTimeMin':float(serTimeMin),\
		'overallDuration':float(overallDuration)}
	print("-"*60)
	print("          Statistics Summary")
	# Log Message
	print("The percentage of empty tasks: "+str(emptyTaskPercentage)+"%")
	print("The average scheduler delay: "+str(schedulerDelayAverage)+" ms")
	print("The maximum scheduler delay: "+str(schedulerDelayMax)+" ms")
	print("The minimum scheduler delay: "+str(schedulerDelayMin)+" ms")
	print("The average task deserialization time: "+str(deserTimeAverage)+" ms")
	print("The maximum task deserialization time: "+str(deserTimeMax)+" ms")
	print("The minimum task deserialization time: "+str(deserTimeMin)+" ms")
	print("The average task execution time: "+str(exeTimeAverage)+" ms")
	print("The maximum task execution time: "+str(exeTimeMax)+" ms")
	print("The minimum task execution time: "+str(exeTimeMin)+" ms")
	print("The average task serialization time: "+str(serTimeAverage)+" ms")
	print("The maximum task serialization time: "+str(serTimeMax)+" ms")
	print("The minimum task serialization time: "+str(serTimeMin)+" ms")
	print("The overall duration time: "+str(overallDuration)+" min")
	print("-"*60)
	# Generate JSON data for web view
	webViewData = {'TimelineData':view_taskTimeline,\
			'DeserTimeData':view_deserTime,\
			'ExeTimeData':view_exeTime,\
			'SerTimeData':view_serTime,\
			'SchedulerData':view_schedulerDelay,\
			'Statistic':statisticData}
				
	# Create JSON file and dump JSON data into it.
	with open(curr_dir+'/webView-'+inputFileName, 'wb') as webView:
		json.dump(webViewData, webView)
		webView.close()
			
		# Copy file to Apache directory
		#shutil.copy2(curr_dir+'/webView.json','/home/pli/Sites')
	
if __name__ == "__main__":
	main()