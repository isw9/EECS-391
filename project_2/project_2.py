import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sns
import numpy as np
from sklearn import model_selection
from sklearn import datasets
import random
from keras.layers import Dense, Activation
from keras.models import Sequential, Model
from array import array

def modelNN(input_dimension):
    model = Sequential()
    model.add(Dense(1,input_dim=input_dimension,activation='sigmoid'))
    model.compile(optimizer='rmsprop',loss='mean_squared_error',metrics=['accuracy'])
    return model

def sigmoid(z):
    return 1 / (1 + np.exp(-z))

data = pd.read_csv('irisdata.csv', sep=',')

def problem_1_a(problem_letter, x_coords_line, y_coords_line):
    colors = ['red', 'blue']
    species = ['setosa', 'virginica', 'versicolor']
    for i in range(1, 3):
        species_df = data[data['species'] == species[i]]
        plt.scatter(species_df['petal_length'],species_df['petal_width'],color=colors[i-1])

    plt.xlabel('petal length (cm)')
    plt.ylabel('petal width (cm)')
    plt.title('Petal Width vs Petal Length - Problem #1(A)')
    if (problem_letter == 'c'):
        plt.plot(x_coords_line, y_coords_line)
        plt.title('Petal Width vs Petal Length - Problem #1(C)')

    plt.show()

def problem_1_b():
    dataset = data.iloc[50:, [2,3]].values
    target = data.iloc[50:, 4].values

    x_train, x_test, y_train, y_test = model_selection.train_test_split(dataset, target, test_size = 0.4)

    #initialize the two weights and bias to be random between 0 and 1
    weight_one = 0.5
    weight_two = 0.5
    bias = -3.5
    error = []


    size = len(x_train)
    for i in range(size):
        z = x_train[i][0]* weight_one + x_train[i][1] * weight_two + bias
        sigmoid_result = sigmoid(z)
        if (y_train[i] == 'versicolor'):
            actual_class = 0
        else:
            actual_class = 1
        error.append(sigmoid_result - actual_class)

    size = len(x_test)
    num_wrong = 0
    num_right = 0
    for i in range(size):
        if (y_test[i] == 'versicolor'):
            actual_class = 0

        else:
            actual_class = 1

        z = x_test[i][0] * weight_one + x_test[i][1] * weight_two + bias
        sigmoid_result = sigmoid(z)
        if (sigmoid_result < 0.5):
            predicted_class = 0
        else:
            predicted_class = 1

        print("actual class: ", actual_class)
        print("predicted class: ", predicted_class)
        if (actual_class != predicted_class):
            num_wrong += 1
        else:
            num_right += 1
        print("--------")
    percent_correct = num_right / (num_right + num_wrong)
    print("part 1b-amount correct: ", percent_correct)

    problem_1_c(weight_one, weight_two, bias)

def problem_1_c(weight_one, weight_two, bias):
    x = -bias / weight_one
    y = -bias / weight_two
    d = y
    c = -y / x
    line_x_coords = [0, x]
    line_y_coords = []
    for i in range(2):
        line_y_coords.append(c * line_x_coords[i] + d)
    problem_1_a('c', line_x_coords, line_y_coords)

def problem_4_a():
    encodeOutput = []
    Input = data.iloc[50:, [2,3]].values
    Output = data.iloc[50:, 4].values
    for i in Output:
        if i == 'versicolor':
            encodeOutput.append(0)
        else:
            encodeOutput.append(1)

    inputTrain, inputVal, outputTrain, outputVal = model_selection.train_test_split(Input,encodeOutput,test_size=0.25,shuffle=True)
    model = modelNN(2)
    model.fit(x=inputTrain,y=outputTrain,epochs=2000, validation_data=(inputVal,outputVal))

def problem_4_b():
    encodeOutput = []
    Input = data.iloc[0:, [0, 1, 2, 3]].values
    Output = data.iloc[0:, 4].values

    for i in Output:
        if i == 'setosa':
            encodeOutput.append(0)
        elif i == 'versicolor':
            encodeOutput.append(1)
        else:
            encodeOutput.append(2)

    inputTrain, inputVal, outputTrain, outputVal = model_selection.train_test_split(Input,encodeOutput,test_size=0.25,shuffle=True)
    model = modelNN(4)
    model.fit(x=inputTrain,y=outputTrain,epochs=2000, validation_data=(inputVal,outputVal))


if __name__ == "__main__":
    problem_1_a('a', [], [])
    problem_1_b()
    #problem_4_a()
    #problem_4_b()
