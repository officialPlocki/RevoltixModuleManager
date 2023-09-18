package me.revoltix.moduleloader.util;

public class ModuleScheduler {

    /**
     * Add a new task to the scheduler
     *
     * @param runnable The Runnable object that you want to schedule.
     * @param time The time in ticks when the task should be executed.
     * @return The number of the task.
     */
    public int scheduleTask(Runnable runnable, int time) {
        return 0;
    }

    /**
     * Remove a task from the scheduler
     *
     * @param task The task number to stop.
     */
    public void stopScheduledTask(int task) {
    }

    /**
     * It runs a task after a certain amount of time.
     *
     * @param runnable The Runnable object that you want to run.
     * @param time The time in ticks to wait before executing the runnable.
     */
    public void timeTask(Runnable runnable, long time) {
    }

}
