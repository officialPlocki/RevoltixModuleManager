package me.revoltix.moduleloader.util;

import java.util.HashMap;

public class ModuleScheduler {

    // Keeping track of the number of tasks that have been scheduled.
    private int runs;
    // This is a hashmap that keeps track of the number of tasks that have been scheduled.
    private final HashMap<Integer, Runnable> numbers;
    // Keeping track of the number of times a task has been scheduled.
    private final HashMap<Runnable, Integer> runnables;
    // Keeping track of the number of times a task has been scheduled.
    private final HashMap<Runnable, Integer> scheduled;

    public ModuleScheduler() {
        runs = 0;
        numbers = new HashMap<>();
        runnables = new HashMap<>();
        scheduled = new HashMap<>();
        new Thread(() -> {
            for(;;) {
                runnables.forEach((runnable, time) -> {
                    scheduled.put(runnable, scheduled.getOrDefault(runnable, 0) + 1);
                    if(scheduled.get(runnable) >= time) {
                        scheduled.remove(runnable);
                        new Thread(runnable).start();
                    }
                });
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Add a new task to the scheduler
     *
     * @param runnable The Runnable object that you want to schedule.
     * @param time The time in ticks when the task should be executed.
     * @return The number of the task.
     */
    public int scheduleTask(Runnable runnable, int time) {
        runs = runs + 1;
        numbers.put(runs, runnable);
        runnables.put(runnable, time);
        return runs;
    }

    /**
     * Remove a task from the scheduler
     *
     * @param task The task number to stop.
     */
    public void stopScheduledTask(int task) {
        Runnable runnable = numbers.get(task);
        runnables.remove(runnable);
        scheduled.remove(runnable);
        numbers.remove(task);
    }

    /**
     * It runs a task after a certain amount of time.
     *
     * @param runnable The Runnable object that you want to run.
     * @param time The time in ticks to wait before executing the runnable.
     */
    public void timeTask(Runnable runnable, long time) {
        new Thread(() -> {
            try {
                Thread.sleep(50*time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runnable.run();
        }).start();
    }

}
