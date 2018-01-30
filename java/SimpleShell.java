import java.io.*;
import java.util.*;

/*
 * Seems largely finished to do some more testing to check if it fully works
 */

public class SimpleShell {
	public static void main(String[] args) throws java.io.IOException {
		String commandLine;
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		FixedQueue<String> history = new FixedQueue<String>(9);
		ProcessBuilder pb = new ProcessBuilder();

		
		while (true) {
			// read what the user entered
			System.out.print("jsh> ");
			commandLine = console.readLine();


			// if the user entered a return, just loop again
			if (commandLine.equals("")) {
				continue;
			}
			
			try {
				// Split the string
				String[] commandList = commandLine.split(" ");

				// Start Process Builder

				if(commandList[0].equals("history")){
					// Get history
					for(int i = 0; i < history.size(); i++){
						System.out.println(Integer.toString(i + 1) + " " + history.get(history.size() - 1 - i));
					}
					
					// bypass
					continue; 
				}

				else if(commandList[0].chars().allMatch(Character::isDigit) || commandList[0].equals("!!")){
					// run previous command via digits
					int i = 0;
					if(commandList[0].equals("!!")){
						i = 1;
					} else {
						i = Integer.parseInt(commandList[0]);
					}
					
					String[] commandList2 = history.get(history.size() - i).split(" ");
					
					if(commandList2[0].equals("cd") || commandList2[0].equals("chdir")){
						// change directory method
						File goDir = null;

						// get current directory .
						File currentDir = pb.directory();
						if (currentDir == null) currentDir = new File(System.getProperty("user.dir"));

						// get home directory ~
						String homePath = System.getProperty("user.home");
						File homeDir = new File(homePath);

						// get parent directory .. 
						File parentDir = new File(currentDir.getAbsolutePath()).getParentFile();

						if(commandList2[1].length() == 1){
							// handle cd . and cd ~
							
							if(commandList2[1].equals(".")){
								// no change to directory
								history.add(commandLine); 	
								continue;
							}

							else if(commandList2[1].equals("~")){
								// goto home directory
								pb.directory(homeDir);
								history.add(commandLine); 	
								continue;
							} else {
								// Doesn't detect invalid directory 
								try{
									goDir = new File(currentDir, commandList2[1]);
									if(!goDir.exists()){
										throw new Exception("Directory does not exist!");
									}
									pb.directory(goDir);
								} catch (Exception e) {
									System.out.println(e.getMessage());							
								}
								history.add(commandLine); 	
								continue;
							}

						} else {
							if(commandList2[1].substring(0,2).equals("./")){
								try{
									goDir = new File(currentDir, commandList[1].substring(2));
									if(!goDir.exists()){
										throw new Exception("Directory does not exist!");
									}
									pb.directory(goDir);
								} catch (Exception e) {
									System.out.println(e.getMessage());
									continue;
								}
								history.add(commandLine); 	
								continue;
							}
							

							else if(commandList2[1].substring(0,2).equals("..")) {
								if(commandList2[1].length() == 2){
									pb.directory(parentDir);
								} else {
										try{
											goDir = new File(currentDir, commandList2[1].substring(3));
											if(!goDir.exists()){
												throw new Exception("Directory does not exist!");
											}
											pb.directory(goDir);
										} catch (Exception e) {
											System.out.println(e.getMessage());
											continue;
										}
								}
								history.add(commandLine); 	
								continue;
							}

							else {
								// eg cd next_dir
								try{
									goDir = new File(currentDir, commandList2[1]);
									if(!goDir.exists()){
										throw new Exception("Directory does not exist!");
									}
									pb.directory(goDir);
								} catch (Exception e) {
									System.out.println(e.getMessage());
									continue;
								}
								history.add(commandLine); 	
								continue;
							}
						}					
					}

					else {
						pb.command(commandList2);
					}
				}

				else if(commandList[0].equals("cd") || commandList[0].equals("chdir")){
					// change directory method
					File goDir = null;

					// get current directory .
					File currentDir = pb.directory();
					if (currentDir == null) currentDir = new File(System.getProperty("user.dir"));

					// get home directory ~
					String homePath = System.getProperty("user.home");
					File homeDir = new File(homePath);

					// get parent directory .. 
					File parentDir = new File(currentDir.getAbsolutePath()).getParentFile();

					if(commandList[1].length() == 1){
						// handle cd . and cd ~
						
						if(commandList[1].equals(".")){
							// no change to directory
							history.add(commandLine); 	
							continue;
						}

						else if(commandList[1].equals("~")){
							// goto home directory
							pb.directory(homeDir);
							history.add(commandLine); 	
							continue;
						} else {
							// Doesn't detect invalid directory 
							try{
								goDir = new File(currentDir, commandList[1]);
								if(!goDir.exists()){
									throw new Exception("Directory does not exist!");
								}
								pb.directory(goDir);
							} catch (Exception e) {
								System.out.println(e.getMessage());							
							}
							history.add(commandLine); 	
							continue;
						}

					} else {
						if(commandList[1].substring(0,2).equals("./")){
							try{
								goDir = new File(currentDir, commandList[1].substring(2));
								if(!goDir.exists()){
									throw new Exception("Directory does not exist!");
								}
								pb.directory(goDir);
							} catch (Exception e) {
								System.out.println(e.getMessage());
								continue;
							}
							history.add(commandLine); 	
							continue;
						}
						

						else if(commandList[1].substring(0,2).equals("..")) {
							if(commandList[1].length() == 2){
								pb.directory(parentDir);
							} else {
									try{
										goDir = new File(currentDir, commandList[1].substring(3));
										if(!goDir.exists()){
											throw new Exception("Directory does not exist!");
										}
										pb.directory(goDir);
									} catch (Exception e) {
										System.out.println(e.getMessage());
										continue;
									}
							}
							history.add(commandLine); 	
							continue;
						}

						else {
							// eg cd next_dir
							try{
								goDir = new File(currentDir, commandList[1]);
								if(!goDir.exists()){
									throw new Exception("Directory does not exist!");
								}
								pb.directory(goDir);
							} catch (Exception e) {
								System.out.println(e.getMessage());
								continue;
							}
							history.add(commandLine); 	
							continue;
						}
					}					
				}

				else {
					// run other commands
					pb.command(commandList);
				}

				// Pass command to process
				Process p = pb.start();
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				for (String line; (line = br.readLine()) != null; ){
					System.out.println(line);
				}
				br.close();
				
				// Add the command into history
				history.add(commandLine); 	

			} catch (IOException e){
				System.out.println(e.getMessage());
			}

		
		}
	}
}
