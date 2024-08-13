import csv
import sys

# Check if the correct number of arguments are provided
if len(sys.argv) != 3:
    print("Usage: python script.py <rows> <cols>")
    sys.exit(1)

# Get the row and column counts from the command-line arguments
rows = int(sys.argv[1])
cols = int(sys.argv[2])

# Create the CSV file
with open('16_by_16_one_100.csv', 'w', newline='') as csvfile:
    writer = csv.writer(csvfile)
    
    # Write the header row
    header_row = [''] + [str(i) for i in range(cols)]
    writer.writerow(header_row)
    
    # Write the data rows
    for i in range(rows):
        row = [str(i)] + [0] * cols
        writer.writerow(row)

print('CSV file created: 16_by_16_one_100.csv')