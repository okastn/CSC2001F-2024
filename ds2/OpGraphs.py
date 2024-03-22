import matplotlib.pyplot as plt 
  
x_insert = [] 
y_insert = [] 
for line in open('results.txt', 'r'): 
    lines = [i for i in line.split()] 
    x_insert.append(lines[0]) 
    y_insert.append(int(lines[1])) 
      
plt.title("Insert comparison operations") 
plt.xlabel("size (n) of the dataset") 
plt.ylabel("number of comparison operations") 
plt.yticks(y_insert) 
plt.plot(x_insert, y_insert, marker = 'o', c = 'm') 
plt.show()

x_search = [] 
y_search = [] 

for line in open('results.txt', 'r'): 
    lines = [i for i in line.split()] 
    x_search.append(lines[0]) 
    y_search.append(int(lines[2])) 
      
plt.title("Search comparison operations") 
plt.xlabel("size (n) of the dataset") 
plt.ylabel("number of comparison operations") 
plt.yticks(y_search) 
plt.plot(x_search, y_search, marker = 'o', c = 'b') 
plt.show()


