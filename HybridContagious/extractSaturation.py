#!/usr/bin/env python3

import warnings
# warnings.filterwarnings('ignore')
import sys
import getopt
import os
import csv
import re
from datetime import datetime
import operator
import matplotlib.pyplot as plt
plt.style.use('ggplot')
import random

import numpy as np
from scipy import stats
import pandas as pd
import seaborn as sns

from multiprocessing import Pool

resultDir = '/raid/lifeworks/working/simulations/socialContagion/bak/rawResults'
# resultDir = '/raid/lifeworks/working/simulations/hybridContagion/indirectContagion'
newResultDir = '/raid/lifeworks/working/simulations/socialContagion/bak/results/saturation'

directResults = resultDir + '/directContagion'
indirectResults = resultDir + '/indirectContagion'
directOutputs = newResultDir + '/directContagion'
indirectOutputs = newResultDir + '/indirectContagion'


import ray


@ray.remote
def getLastStep(directory, workingDir, outputDir):
    pDirs = [dirName for dirName in os.listdir(os.path.join(
        workingDir, directory)) if not os.path.isfile(os.path.join(workingDir, directory, dirName))]
    outputfile = outputDir + '/' + directory + '.txt'
    output = []
    for pDir in pDirs:
        matched = re.match('probability-([0-9\.]*)', pDir)
        prob = float(matched.group(1))
        fileNames = [filename for filename in os.listdir(os.path.join(
            workingDir, directory, pDir)) if os.path.isfile(os.path.join(workingDir, directory, pDir, filename))]
        lastSteps = []
        for filename in fileNames:
            allsteps = np.genfromtxt(os.path.join(
                workingDir, directory, pDir, filename))
            dims = allsteps.shape
            lastStep = []
            if len(dims) == 2 and dims[1] == 2:
                lastStep = allsteps[dims[0]-1]
            elif len(dims) == 1 and dims[0] == 2:
                lastStep = allsteps
            else:
                print(directory + '/' + pDir + '/' +
                      filename + ' has wrong dimesion')
                # sys.exit()

            if lastStep[1] != 0.99:
                lastStep[0] = 3e7
            lastSteps.append(lastStep)
        lastSteps = np.array(lastSteps)
        means = np.mean(lastSteps, axis=0)
        stds = np.std(lastSteps, axis=0)
        output.append([prob, means[1], stds[1], means[0], stds[0], np.amin(
            lastSteps, axis=0)[0], np.amax(lastSteps, axis=0)[0]])
    output = np.array(output)
    np.savetxt(outputfile, output[output[:, 0].argsort()])
    # return([outputfile, output[output[:, 0].argsort()]])


ray.init()

workingDir = directResults
outputDir = directOutputs
dirList = [dirName for dirName in os.listdir(workingDir) if not os.path.isfile(os.path.join(workingDir, dirName))]

ray.get([getLastStep.remote(dirName, workingDir, outputDir) for dirName in dirList])
print('successful')

workingDir = indirectResults
outputDir = indirectOutputs
dirList = [dirName for dirName in os.listdir(workingDir) if not os.path.isfile(os.path.join(workingDir, dirName))]

ray.get([getLastStep.remote(dirName, workingDir, outputDir) for dirName in dirList])
print('successful')
