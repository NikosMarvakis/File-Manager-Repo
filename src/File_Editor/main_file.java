package File_Editor;

public class main_file {
	public static void main(String[] args) {		

		//show possible commands to choose from
		Functions.get_commands();
		System.out.println();
		
		//take input
		System.out.print("Enter input (use '>' to give multiple input values at once): ");
		String input = Functions.input_function();
		
		//create padded array to copy later commands into
		String[] padded_input = {null, null, null, null};

		//main loop
			//check if input == "exit"
		while (!input.equals("exit")){

			//split input by ">" sign and strip both sides from whitespaces
			String[] split_input = input.split(">");
			for(int i = 0; i < split_input.length; i++) {
				split_input[i] = split_input[i].trim();
			}

			//copy split commands into padded array
			for (int i = 0; i < split_input.length; i++) {
				padded_input[i] = split_input[i];
			}

			switch(padded_input[0]) {
				//shows the files in the given path
				case "list":
					Functions.list_dir();
					break;

				//shows the current path
				case "path":
					System.out.println(Functions.get_path());
					break;

				//shows the available commands to choose from
				case "info":
					Functions.get_commands();
					System.out.println();
					break;

				//change the current directory with another one
				case "chdir":
					Functions.change_dir(padded_input[1]);
					break;

				//go one directory outside
				case "prevdir":
					Functions.prev_dir();
					break;

				//create a new file
				case "make file":
					Functions.new_file(padded_input[1]);
					break;

				//delete a file
				case "delete file":
					Functions.del_file(padded_input[1]);
					break;
				
				//rename a file
				case "rename file":
					Functions.rename_file(padded_input[1], padded_input[2]);
					break;
					
				//read a file
				case "read file":
					Functions.read_file(padded_input[1]);
					break;
					
				//write into a file
				case "write file":
					Functions.write_file(padded_input[1], padded_input[2]);
					break;
					
				//clear a file
				case "clear file":
					Functions.clear_file(padded_input[1]);
					break;
					
				//copy a file
				case "copy file":
					Functions.copy(padded_input[1], padded_input[2]);
					break;
					
				//move a file
				case "move file":
					Functions.move(padded_input[1], padded_input[2]);
					break;	
					
				//create a new directory (folder)
				case "make dir":
					Functions.new_dir(padded_input[1], padded_input[2]);
					break;
					
				//delete a directory (folder)
				case "delete dir":
					Functions.del_dir(padded_input[1]);
					break;
					
				//rename a directory / folder
				case "rename dir":
					Functions.rename_dir(padded_input[1], padded_input[2]);
					break;
					
				//move a folder to another path / location
				case "move dir":
					Functions.move_dir(padded_input[1], padded_input[2]);
					break;
			}

			//remove the padding
			for(int i = 0; i < padded_input.length; i++) {
				padded_input[i] = null;
			}

			//continue or break loop
			System.out.print("Enter input (use '>' to give multiple input values at once): ");
			input = Functions.input_function();
		}
	}
}
