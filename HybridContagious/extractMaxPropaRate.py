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

    togetherPlot = outputDir + '/together-average-rates' + directory + '.pdf'
    outdir = outputDir + '/' + directory
    try:
        os.makedirs(outdir)
    except OSError as e:
        if e.errno != os.errno.EEXIST:
            raise
    togethx = np.array([])
    togethdata = []
    outputfile = outputDir + '/' + directory + '.txt'
    output = []
    for pDir in pDirs:
        matched = re.match('probability-([0-9\.]*)', pDir)
        probability = float(matched.group(1))
        seperatePlot = outdir + '/seperate-trajectory-rates-' + matched.group(1) + '.pdf'
        averagePlot = outdir + '/average-trajectory-rates-' + matched.group(1) + '.pdf'
        averageData = outdir + '/average-trajectory-rates-' + matched.group(1) + '.txt'
        fileNames = [filename for filename in os.listdir(os.path.join(workingDir, directory, pDir)) if os.path.isfile(os.path.join(workingDir, directory, pDir, filename))]
        data = []
        maxStep = 0
        xaxis = np.array([])
        for filename in fileNames:
            allsteps = np.genfromtxt(os.path.join(workingDir, directory, pDir, filename))
            dims = allsteps.shape
            if len(dims) == 2 and dims[1] == 2:
                allsteps = np.transpose(allsteps)
            elif len(dims) == 1 and dims[0] == 2:
                allsteps = np.array([allsteps, allsteps + np.array([1, allsteps[1]])]).transpose()
            else:
                print(directory + '/' + pDir + '/' + filename + ' has wrong dimesion')
                # sys.exit()

            tempMax = allsteps[0].max()
            if tempMax > maxStep:
                maxStep = tempMax
            data.append(allsteps)
            xaxis = np.concatenate((xaxis, allsteps[0]), axis=None)

        xaxis = np.unique(xaxis)
        steps = len(xaxis)
        yaxis = np.zeros(steps)
        dxaxis = (xaxis[1:] + xaxis[:-1]) / 2
        fig = plt.figure()
        # plt.yscale('log')
        # plt.ylim(0.0001, 2)
        # plt.xlim(0,3e7)
        plt.xlim(0,xaxis.max())
        for replicate in data:
            tempy = np.zeros(steps)
            di = 0
            for ai in range(len(xaxis)):
                if di >= len(replicate[0]):
                    yaxis[ai] += replicate[1,-1]
                    tempy[ai] = replicate[1,-1]
                else:
                    if xaxis[ai] == replicate[0,di]:
                        yaxis[ai] += replicate[1,di]
                        tempy[ai] = replicate[1,di]
                        di += 1
                    elif xaxis[ai] < replicate[0,di]:
                        yaxis[ai] += replicate[1,di-1]
                        tempy[ai] = replicate[1,di-1]
                    else:
                        print('something went wrong, ax is larger than dx')
            tempdy = np.diff(tempy)/np.diff(xaxis)
            plt.plot(dxaxis, tempdy)
        #plt.show()
        fig.savefig(seperatePlot,bbox_inches='tight')
        plt.close(fig)

        yaxis /= len(data)
        dyaxis = np.diff(yaxis)/np.diff(xaxis)

        fig = plt.figure()
        # plt.yscale('log')
        # plt.ylim(0.0001, 2)
        # plt.xlim(0,3e7)
        plt.xlim(0,xaxis.max())
        plt.plot(dxaxis, dyaxis)
        fig.savefig(averagePlot,bbox_inches='tight')
        plt.close(fig)

        togethx = np.concatenate((togethx,dxaxis), axis=None)
        aveData = np.stack((dxaxis, dyaxis))
        np.savetxt(averageData, aveData)
        togethdata.append([aveData,probability])

        maxrate = dyaxis.max()
        output.append([probability, maxrate])

    output = np.array(output)
    np.savetxt(outputfile, output[output[:, 0].argsort()])

    togethx = np.unique(togethx)

    fig = plt.figure()
    # plt.yscale('log')
    # plt.ylim(0.0001, 2)
    # plt.xlim(0,3e7)
    plt.xlim(0,togethx.max())
    for prob in togethdata:
        plt.plot(prob[0][0], prob[0][1])

    fig.savefig(togetherPlot,bbox_inches='tight')
    plt.close(fig)


ray.init()

workingDir = directResults
outputDir = directOutputs
dirList = [dirName for dirName in os.listdir(workingDir) if not os.path.isfile(os.path.join(workingDir, dirName))]

ray.get([plotDynamics.remote(dirName, workingDir, outputDir) for dirName in dirList])
print('successful')

workingDir = indirectResults
outputDir = indirectOutputs
dirList = [dirName for dirName in os.listdir(workingDir) if not os.path.isfile(os.path.join(workingDir, dirName))]

ray.get([plotDynamics.remote(dirName, workingDir, outputDir) for dirName in dirList])
print('successful')


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
