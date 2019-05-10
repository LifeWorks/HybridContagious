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
newResultDir = '/raid/lifeworks/working/simulations/socialContagion/bak/results/dynamics'

directResults = resultDir + '/directContagion'
indirectResults = resultDir + '/indirectContagion'
directOutputs = newResultDir + '/directContagion'
indirectOutputs = newResultDir + '/indirectContagion'


import ray


@ray.remote
def plotDynamics(directory, workingDir, outputDir):
    pDirs = [dirName for dirName in os.listdir(os.path.join(workingDir, directory)) if not os.path.isfile(os.path.join(workingDir, directory, dirName))]

    togetherPlot = outputDir + '/' + directory + '/together-average-' + directory + '.png'
    togetherPlotNL = outputDir + '/' + directory + '/together-average-NL-' + directory + '.png'
    togethx = np.array([])
    togethdata = []
    for pDir in pDirs:
        matched = re.match('probability-([0-9\.]*)', pDir)
        probability = float(matched.group(1))
        outdir = outputDir + '/' + directory + '/'+ pDir
        try:
            os.makedirs(outdir)
        except OSError as e:
            if e.errno != os.errno.EEXIST:
                raise
        seperatePlot = outdir + '/seperate-trajectory-' + matched.group(1) + '.png'
        averagePlot = outdir + '/average-trajectory-' + matched.group(1) + '.png'
        averageData = outdir + '/average-trajectory-' + matched.group(1) + '.txt'
        fileNames = [filename for filename in os.listdir(os.path.join(workingDir, directory, pDir)) if os.path.isfile(os.path.join(workingDir, directory, pDir, filename))]
        data = []
        maxStep = 0
        xaxis = np.array([])
        for filename in fileNames:
            allsteps = np.genfromtxt(os.path.join(workingDir, directory, pDir, filename)).transpose()
            dims = allsteps.shape
            if len(dims) == 1 and dims[0] == 2:
                allsteps = np.array([allsteps])
            tempMax = allsteps[0].max()
            if tempMax > maxStep:
                maxStep = tempMax
            data.append(allsteps)
            xaxis = np.concatenate((xaxis, allsteps[0]), axis=None)

        xaxis = np.unique(xaxis)
        yaxis = np.zeros(len(xaxis))
        fig = plt.figure()
        plt.yscale('log')
        plt.ylim(0.001, 1)
        plt.xlim(0,xaxis.max())
        for replicate in data:
            plt.plot(replicate[0], replicate[1])
            di = 0
            for ai in range(len(xaxis)):
                if di >= len(replicate[0]):
                    yaxis[ai] += replicate[1,-1]
                else:
                    if xaxis[ai] == replicate[0,di]:
                        yaxis[ai] += replicate[1,di]
                        di += 1
                    elif xaxis[ai] < replicate[0,di]:
                        yaxis[ai] += replicate[1,di-1]
                    else:
                        print('something went wrong, ax is larger than dx')
        #plt.show()
        fig.savefig(seperatePlot,bbox_inches='tight')
        plt.close(fig)

        yaxis /= len(data)

        fig = plt.figure()
        plt.yscale('log')
        plt.ylim(0.001, 1)
        plt.xlim(0,xaxis.max())
        plt.plot(xaxis, yaxis)
        fig.savefig(averagePlot,bbox_inches='tight')
        plt.close(fig)

        togethx = np.concatenate((togethx,xaxis), axis=None)
        aveData = np.stack((xaxis, yaxis))
        np.savetxt(aveData, averageData)
        togethdata.append(aveData)

    togethx = np.unique(togethx)

    fig = plt.figure()
    plt.yscale('log')
    plt.ylim(0.001, 1)
    plt.xlim(0,togethx.max())
    # labels = []
    for prob in togethdata:
        plt.plot(prob[0], prob[1])
        # labels.append(r'$p = %f$' % (probability))

    # plt.legend(labels, ncol=4, loc='upper center', 
    #         bbox_to_anchor=[0.5, 1.1],
    #         columnspacing=1.0, labelspacing=0.0,
    #         handletextpad=0.0, handlelength=1.5,
    #         fancybox=True, shadow=True)
    fig.savefig(togetherPlotNL,bbox_inches='tight')
    plt.close(fig)

    fig = plt.figure()
    plt.yscale('log')
    plt.ylim(0.001, 1)
    plt.xlim(0,togethx.max())
    labels = []
    for prob in togethdata:
        plt.plot(prob[0], prob[1])
        labels.append(r'$p = %f$' % (probability))

    plt.legend(labels, ncol=4, loc='upper center', 
            bbox_to_anchor=[0.5, 1.1],
            columnspacing=1.0, labelspacing=0.0,
            handletextpad=0.0, handlelength=1.5,
            fancybox=True, shadow=True)
    fig.savefig(togetherPlot,bbox_inches='tight')
    plt.close(fig)

    print('successful')

ray.init()

workingDir = directResults
outputDir = directOutputs

dirList = [dirName for dirName in os.listdir(workingDir) if not os.path.isfile(os.path.join(workingDir, dirName))]
ray.get([plotDynamics.remote(dirName, workingDir, outputDir) for dirName in dirList])

workingDir = indirectResults
outputDir = indirectOutputs

dirList = [dirName for dirName in os.listdir(workingDir) if not os.path.isfile(os.path.join(workingDir, dirName))]
ray.get([plotDynamics.remote(dirName, workingDir, outputDir) for dirName in dirList])


# test


# import matplotlib.pyplot as plt
# import numpy as np

# num_plots = 20

# # Have a look at the colormaps here and decide which one you'd like:
# # http://matplotlib.org/1.2.1/examples/pylab_examples/show_colormaps.html
# #colormap = plt.cm.gist_ncar
# #plt.gca().set_color_cycle([colormap(i) for i in np.linspace(0, 0.9, num_plots)])

# # Plot several different functions...
# x = np.arange(10)
# labels = []
# for i in range(1, num_plots + 1):
#     plt.plot(x, i * x + 5 * i)
#     labels.append(r'$y = %ix + %i$' % (i, 5*i))

# # I'm basically just demonstrating several different legend options here...
# plt.legend(labels, ncol=4, loc='upper center', 
#            bbox_to_anchor=[0.5, 1.1], 
#            columnspacing=1.0, labelspacing=0.0,
#            handletextpad=0.0, handlelength=1.5,
#            fancybox=True, shadow=True)

# plt.show()
