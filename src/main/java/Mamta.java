import java.util.Scanner;
import java.util.ArrayList;

public class Mamta {
    private static final ArrayList<Task> history = new ArrayList<Task>();


    public static String greet() {
        return "Hello! I'm Mamta\nWhat can I do for you?";
    }

    public static String exit() {
        return "Bye. Hope to see you again soon!";
    }

    public static String echo(String taskType, String command, int taskNum, String deadline, String endTime) {
        String output = "";
        switch (command) {
            case "bye":
                return String.format("------------------------------------------\n%s\n------------------------------------------", Mamta.exit());
            case "list":
                StringBuilder returnOutput = new StringBuilder();
                int count = 0;
                returnOutput.append("------------------------------------------\n");
                for (Task item : history) {
                    count += 1;
                    returnOutput.append(String.format("%d. %s\n", count, history.get(count - 1)));
                }
                returnOutput.append("------------------------------------------");
                return returnOutput.toString();
            case "mark":
                history.set(taskNum - 1, history.get(taskNum - 1).markDone());
                return String.format("------------------------------------------\nNice! I've marked this task as done\n%s\n------------------------------------------", history.get(taskNum - 1));
            case "unmark":
                history.set(taskNum - 1, history.get(taskNum - 1).unmarkTask());
                return String.format("------------------------------------------\nOK, I've marked this task as not done yet:\n%s\n------------------------------------------", history.get(taskNum - 1));
            default:
                if (taskType.equals("todo")) {
                    Todo myTodo = new Todo(command);
                    history.add(myTodo);
                    output = String.format("------------------------------------------\nGot it. I've added this task: \n%s\nNow you have %d tasks in the list\n------------------------------------------", myTodo, history.size());
                } else if (taskType.equals("deadline")) {
                    Deadline myDead = new Deadline(command, deadline);
                    history.add(myDead);
                    output = String.format("------------------------------------------\nGot it. I've added this task: \n%s\nNow you have %d tasks in the list\n------------------------------------------", myDead, history.size());
                } else if (taskType.equals("event")) {
                    Event myEve = new Event(command, deadline, endTime);
                    history.add(myEve);
                    output = String.format("------------------------------------------\nGot it. I've added this task: \n%s\nNow you have %d tasks in the list\n------------------------------------------", myEve, history.size());
                } else {
                    //handling the default case where its not a tracked task type
                    output = String.format("------------------------------------------\nGot it. I've added this task: %s\nNow you have %d tasks in the list\n------------------------------------------", command, history.size());
                    history.add(new Task(command));
                }
                break;
        }
        return output;

    }


    public static void main(String[] args) {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Hello from\n" + logo);
        System.out.println(Mamta.greet());

        String userOutput = "";
        while (!userOutput.equals("bye")) {
            Scanner scanner = new Scanner(System.in);
            userOutput = scanner.nextLine();
            String[] splitOutput = userOutput.split(" ");

            String word = "";
            int taskNum = -1;

            //in the case user wants to mark/unmark
            if (splitOutput[0].equals("mark") || splitOutput[0].equals("unmark")) {
                word = splitOutput[0];
                taskNum = Integer.parseInt(splitOutput[1]);
                System.out.println(Mamta.echo("default", word, taskNum, "", ""));
            } else if (splitOutput[0].equals("todo")) {
                StringBuilder task = new StringBuilder();
                for (int i = 1; i < splitOutput.length; i++) {
                    task.append(splitOutput[i]).append(" ");
                }
                System.out.println(Mamta.echo(splitOutput[0], task.toString(), taskNum,"", ""));
            } else if (splitOutput[0].equals("deadline") || splitOutput[0].equals("event")) {

                StringBuilder task = new StringBuilder();
                StringBuilder deadline = new StringBuilder();
                StringBuilder endTime = new StringBuilder();
                boolean reachedBy = false;
                boolean reachedTo = false;
                for (int i = 1; i < splitOutput.length; i++) {
                    if ((!reachedBy && !reachedTo) && (!splitOutput[i].equals("/by") && !splitOutput[i].equals("/from"))) {
                        task.append(splitOutput[i]).append(" ");
                    } else if (!reachedTo && (splitOutput[i].equals("/by") || splitOutput[i].equals("/from"))) {
                        reachedBy = true;
                    } else if (reachedBy && (!splitOutput[i].equals("/to"))) {
                        deadline.append(i + 1 == splitOutput.length ? splitOutput[i] : splitOutput[i] + " ");
                    } else if (splitOutput[i].equals("/to")) {
                        reachedTo = true;
                        reachedBy = false;
                    } else if (reachedTo){
                        endTime.append(i + 1 == splitOutput.length ? splitOutput[i] : splitOutput[i] + " ");
                    }
                }
                System.out.println(Mamta.echo(splitOutput[0], task.toString(), taskNum, deadline.toString(), endTime.toString()));

            } else {
                System.out.println(Mamta.echo("default", userOutput, taskNum, "", ""));
            }



        }
    }
}
