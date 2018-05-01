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

resultDir = '/raid/lifeworks/working/simulations/hybridContagion/dataBak'
newResultDir = '/raid/lifeworks/working/simulations/hybridContagion/results/rawResults/saturationLevel'

dirList = [dirName for dirName in os.listdir(
    resultDir) if not os.path.isfile(os.path.join(resultDir, dirName))]


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
            if len(dims) == 2 and dims[1] == 2:
                lastSteps.append(allsteps[dims[0]-1])
            elif len(dims) == 1 and dims[0] == 2:
                lastSteps.append(allsteps)
            else:
                print(directory + '/' + pDir + '/' +
                      filename + ' has wrong dimesion')
                # sys.exit()
        lastSteps = np.array(lastSteps)
        output.append([prob, np.mean(lastSteps, axis=0)[1], np.mean(
            lastSteps, axis=0)[0], np.amax(lastSteps, axis=0)[0]])
    output = np.array(output)
    np.savetxt(outputfile, output[output[:, 0].argsort()])


pool = Pool(processes=47)

list(pool.map(getLastStep, dirList))
