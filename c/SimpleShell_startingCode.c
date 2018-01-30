#include <stdio.h>
#include<stdlib.h>
#include<errno.h>
#include<unistd.h>
#include<string.h>
#include<ctype.h>
#define MAX_INPUT 8192

int main(){

	char command[MAX_INPUT];//to store users command

	while(1){
		//Q1
		printf("csh> ");
		fgets(command, MAX_INPUT, stdin); //take input from user
		//printf("command %s\n",command);

		//-----Case 1, create the external process and execute the command in that process-----
		int system(const char * command);

		//-----Case 2, change directory-----
		//starts with cd
		int chdir(const char *path);



		//-----Case3, History-----
		//check if user enters history option

	}
}
