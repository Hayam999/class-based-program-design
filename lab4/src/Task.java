import tester.*;

interface ILoTask {
    /*
    Returns a linear list of all tasks on the list along with their subtasks and nested subtasks.
    Note: current task will be part of it's linearSubTasks
     */
    ILoTask linearSubTasks();
    ILoTask linearSubTasksHelper(ILoTask tasksAcc);
}    


class Task {
    int id;
    ILoTask subtasks;

    Task(int id, ILoTask subtasks) {
        this.id = id;
        this.subtasks = subtasks;
    }

    public ILoTask collectSubTasks(ILoTask tasksAcc) {
        return subtasks.linearSubTasksHelper(new ConsLoTask(this, tasksAcc));
    }
}

class MtLoTask implements ILoTask{
    MtLoTask() {}

    public ILoTask linearSubTasks() {
        return new MtLoTask();
    }

    public ILoTask linearSubTasksHelper(ILoTask tasksAcc) {
        return tasksAcc;
    }
}    

class ConsLoTask implements ILoTask {
    Task first;
    ILoTask rest;

    ConsLoTask(Task first, ILoTask rest) {
        this.first = first;
        this.rest = rest;
    }  
    
    public ILoTask linearSubTasks() {
        return linearSubTasksHelper(new MtLoTask());
    }


    public ILoTask linearSubTasksHelper(ILoTask tasksAcc) {
        /*  we should ask this.first "which is a task" to add all it's subtasks
            to the task accumulator.
        */
        return this.rest.linearSubTasksHelper(this.first.collectSubTasks(tasksAcc));
    }
}   


class ExamplesTask {
    ILoTask empty = new MtLoTask();
    Task t1 = new Task(1, empty);
    Task t2 = new Task(2, new ConsLoTask(t1, empty));
    Task t3 = new Task(3, new ConsLoTask(t2, new ConsLoTask(t1, empty)));
    Task t4 = new Task(4, empty);
    ILoTask chors = new ConsLoTask(t1,
        new ConsLoTask(t2,
            new ConsLoTask(t3, 
                new ConsLoTask(t4, empty))));
  

    Task honeyDo = new Task(9, chors);

    Task t5 = new Task(5, empty);

    ILoTask allTasks = new ConsLoTask(t5, new ConsLoTask(honeyDo, chors));

    boolean testTask(Tester t) {
        return t.checkExpect(t1.subtasks, new MtLoTask()) &&
               t.checkExpect(t2.subtasks.linearSubTasks(), new ConsLoTask(t1, empty)) &&
               t.checkExpect(t3.subtasks.linearSubTasks(), new ConsLoTask(t1,
                 new ConsLoTask(t1, new ConsLoTask(t2, new MtLoTask()))));
    }

    public static void main(String[] args) {
        // ============================================================
        // DEBUG MODE: use this when you want to set a breakpoint and
        // step through a specific method. Tester.runReport is NOT used
        // here, so there's no 60ms watchdog timeout to fight with the
        // debugger -- you can pause for as long as you want.
        //
        // To use it: comment out the "NORMAL MODE" line below, uncomment
        // this block, edit the method call to whatever you're currently
        // debugging, put your breakpoint inside that method, then hit
        // Debug (F5) on this file/config.
        // ------------------------------------------------------------
         ExamplesTask tasks = new ExamplesTask();
         ILoTask result = tasks.t3.subtasks.linearSubTasks(); 
         System.out.println(result);
        // ============================================================

        // NORMAL MODE: runs the full test suite (has the 60ms timeout,
        // fine for a plain run, awkward if you're paused at a breakpoint)
        Tester.runReport(new ExamplesTask(), false, false);
    }
}
