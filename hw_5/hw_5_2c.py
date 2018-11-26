import matplotlib.pyplot as plt
import pandas as pd
import random
import numpy as np

# hypothesis class so I can treat each hypothesis as an object
class Hypothesis:
	def __init__(self, cherry, lime, prior):
		self.cherry = cherry
		self.lime = lime
		self.prior = prior

# returns the probability that the next candy is cherry or lime
def next_candy_multiplication_cherry(h1, h2, h3, h4, h5):
    return (h1.cherry * h1.prior) + (h2.cherry * h2.prior) + (h3.cherry * h3.prior) + (h4.cherry * h4.prior) + (h5.cherry * h5.prior)

def next_candy_multiplication_lime(h1, h2, h3, h4, h5):
    return (h1.lime * h1.prior) + (h2.lime * h2.prior) + (h3.lime * h3.prior) + (h4.lime * h4.prior) + (h5.lime * h5.prior)

# the
threshold = [100, 75, 50, 25]
for i in range(4):
  running_average_y1 = [.1]
  running_average_y2 = [.2]
  running_average_y3 = [.4]
  running_average_y4 = [.2]
  running_average_y5 = [.1]
  for m in range(100):
    running_average_y1.append(0)
    running_average_y2.append(0)
    running_average_y3.append(0)
    running_average_y4.append(0)
    running_average_y5.append(0)

  for j in range(200):
    h1 = Hypothesis(1, 0, .1)
    h2 = Hypothesis(.75, .25, .2)
    h3 = Hypothesis(.5, .5, .4)
    h4 = Hypothesis(.25, .75, .2)
    h5 = Hypothesis(0, 1, .1)

    x = [0]
    y_1 = [.1]
    y_2 = [.2]
    y_3 = [.4]
    y_4 = [.2]
    y_5 = [.1]
    next_cherry = [.5]
    next_lime = [.5]
    for number in range(100):
      x.append(number)
    lime = 0
    cherry = 0

    candy_list = []
    for number in range(100):
      next_candy_cherry = next_candy_multiplication_cherry(h1, h2, h3, h4, h5)
      next_candy_lime = next_candy_multiplication_lime(h1, h2, h3, h4, h5)
      random_int = random.randint(1,100)
      if (random_int <= threshold[i]):
        candy_list.append('c')
      else:
        candy_list.append('l')

      if (candy_list[number] == 'c'):
        h1.prior = (h1.cherry * h1.prior) / next_candy_cherry
        h2.prior = (h2.cherry * h2.prior) / next_candy_cherry
        h3.prior = (h3.cherry * h3.prior) / next_candy_cherry
        h4.prior = (h4.cherry * h4.prior) / next_candy_cherry
        h5.prior = (h5.cherry * h5.prior) / next_candy_cherry

      else:
        h1.prior = (h1.lime * h1.prior) / next_candy_lime
        h2.prior = (h2.lime * h2.prior) / next_candy_lime
        h3.prior = (h3.lime * h3.prior) / next_candy_lime
        h4.prior = (h4.lime * h4.prior) / next_candy_lime
        h5.prior = (h5.lime * h5.prior) / next_candy_lime

      next_candy_cherry = next_candy_multiplication_cherry(h1, h2, h3, h4, h5)
      next_candy_lime = next_candy_multiplication_lime(h1, h2, h3, h4, h5)
      y_1.append(h1.prior)
      y_2.append(h2.prior)
      y_3.append(h3.prior)
      y_4.append(h4.prior)
      y_5.append(h5.prior)
      running_average_y1[number+1] = running_average_y1[number+1] + h1.prior
      running_average_y2[number+1] = running_average_y2[number+1] + h2.prior
      running_average_y3[number+1] = running_average_y3[number+1] + h3.prior
      running_average_y4[number+1] = running_average_y4[number+1] + h4.prior
      running_average_y5[number+1] = running_average_y5[number+1] + h5.prior
      next_cherry.append(next_candy_cherry)
      next_lime.append(next_candy_lime)

  for k in range(100):
    running_average_y1[k+1] = running_average_y1[k+1] / 200
    running_average_y2[k+1] = running_average_y2[k+1] / 200
    running_average_y3[k+1] = running_average_y3[k+1] / 200
    running_average_y4[k+1] = running_average_y4[k+1] / 200
    running_average_y5[k+1] = running_average_y5[k+1] / 200


  plt.plot(x, running_average_y1, label='h1')
  plt.plot(x, running_average_y2, label='h2')
  plt.plot(x, running_average_y3, label='h3')
  plt.plot(x, running_average_y4, label='h4')
  plt.plot(x, running_average_y5, label='h5')


  plt.xlabel('number samples')
  plt.ylabel('posterior probability of hypothesis')


  plt.legend()

  plt.show()
