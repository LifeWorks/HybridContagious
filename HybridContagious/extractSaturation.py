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

resultDir = '/raid/lifeworks/working/opinionDyn/hybridContagion/HybridContagious/HybridContagious/data'
# resultDir = '/raid/lifeworks/working/simulations/hybridContagion/indirectContagion'
newResultDir = '/raid/lifeworks/working/simulations/HybridContagious/HybridContagious/results/analysed/saturationLevel'

dirList = [dirName for dirName in os.listdir(
    resultDir) if not os.path.isfile(os.path.join(resultDir, dirName))]

# import ray


# @ray.remote
def getLastStep(directory):
    pDirs = [dirName for dirName in os.listdir(os.path.join(
        resultDir, directory)) if not os.path.isfile(os.path.join(resultDir, directory, dirName))]
    outputfile = newResultDir + '/' + directory + '.txt'
    output = []
    for pDir in pDirs:
        matched = re.match('probability-([0-9\.]*)', pDir)
        prob = float(matched.group(1))
        fileNames = [filename for filename in os.listdir(os.path.join(
            resultDir, directory, pDir)) if os.path.isfile(os.path.join(resultDir, directory, pDir, filename))]
        lastSteps = []
        for filename in fileNames:
            allsteps = np.genfromtxt(os.path.join(
                resultDir, directory, pDir, filename))
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


pool = Pool(processes=47)
list(pool.map(getLastStep, dirList))

# ray.init()
# allOutput = ray.get([getLastStep.remote(dirName) for dirName in dirList])

# for output in allOutput:
#     np.savetxt(output[0], output[1])
