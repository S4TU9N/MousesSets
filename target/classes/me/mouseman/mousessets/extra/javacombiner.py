import os
from pathlib import Path

def combine_java_files(source_dir, output_file):
    """
    Combines all .java files in the source directory and its subfolders
    into a single text file.
    """
    source_path = Path(source_dir)
    
    # Open the output file in write mode ('w') with UTF-8 encoding
    with open(output_file, 'w', encoding='utf-8') as outfile:
        
        # .rglob('*.java') recursively finds all matching files
        for java_file in source_path.rglob('*.java'):
            try:
                with open(java_file, 'r', encoding='utf-8') as infile:
                    content = infile.read()
                    
                    # Write a separator and the relative file path for clarity
                    outfile.write(f"{'='*80}\n")
                    outfile.write(f"File: {java_file.relative_to(source_path)}\n")
                    outfile.write(f"{'='*80}\n\n")
                    
                    # Write the actual Java code
                    outfile.write(content)
                    outfile.write("\n\n") # Add spacing between files
                    
                print(f"Processed: {java_file.relative_to(source_path)}")
                
            except Exception as e:
                print(f"Error reading {java_file.name}: {e}")

if __name__ == "__main__":
    # --- Configuration ---
    # Replace with the path to your main folder containing the Java files
    TARGET_DIRECTORY = "C:/Users/camde/Github Repos/MousesSets/src/main" 
    
    # The name of the text file you want to create
    OUTPUT_FILENAME = "combined_java_code.txt" 
    
    print("Starting file combination...")
    combine_java_files(TARGET_DIRECTORY, OUTPUT_FILENAME)
    print(f"\nDone! All Java files have been combined into: {OUTPUT_FILENAME}")