class WorkingClass implements Runnable{
@Override
    public void run() {
        //Фоновая работа
    }
}

WorkingClass workingClass = new WorkingClass();
Thread thread = new Thread(workingClass);
thread.start();