package SimStation;

import mvc.Model;
import tools.Utilities;

import java.io.Serializable;
import java.util.*;

public class Simulation extends Model implements Serializable {

    private transient Timer timer;
    private int clock = 0;
    protected ArrayList<Agent> Agents = new ArrayList<>();

    public Simulation(){
        populate();
        for(Agent a : Agents){
            Thread thread = new Thread(a);
            a.setAgentThread(thread);
            System.out.println("Thread given");
        }
    }

    public void changeState(String heading){
        if(heading.equals("Start")){
            startTimer();
            for(Agent a : Agents){
                if(!a.started){
                    a.started = true;
                    a.getAgentThread().start();
                }
            }
        }
        if(heading.equals("Suspend")){
            for(Agent a : Agents){
                if(a.getAgentThread() != null)
                    a.suspend();
            }
            if(timer != null)
                stopTimer();
        }
        if(heading.equals("Resume")){
            if(Agents.get(0).getAgentThread() != null){
                startTimer();
            }
            else{
                for(Agent a : Agents){
                    Thread thread = new Thread(a);
                    a.setAgentThread(thread);
                    System.out.println("Thread given");
                    }
            }
            for(Agent a : Agents){
                a.resume();
            }
        }
        if(heading.equals("Stop")){
            for(Agent a : Agents){
                a.stop();
            }
            stopTimer();
        }
        if(heading.equals("Stats")){
            getStats();
        }
    }

    public void getStats(){
        Utilities.inform(new String[]{"#Agents = "+Agents.size(),"clock = "+clock});
    }

    public void addAgent(Agent a){ Agents.add(a); }

    public ArrayList<Agent> getAgents() {
        return Agents;
    }


    //Time methods

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new ClockUpdater(), 1000, 1000);
    }

    private void stopTimer() {
        timer.cancel();
        timer.purge();
    }

    private class ClockUpdater extends TimerTask {
        public void run() {
            clock++;
            //changed();
        }
    }

    public int getClock(){
        return clock;
    }
    //Can these be abstract?
    public Agent getNeighbor(Agent a, int radius){
        int searches = 0;
        int ind = Utilities.rng.nextInt(Agents.size());
        while (searches < Agents.size()){
            Agent b = (Agent)Agents.get(ind);
            if (distance(a, b) < radius){
                if (a != b){
                    return b;
                }
            }
            searches++;
            if (ind == (Agents.size()-1)) {
                ind = 0;
            }
            else { ind ++; }
        }
        return null;
    }

    public double distance(Agent a, Agent b){
        int j, k;
        j = (a.x-b.x)*(a.x-b.x);
        k = (a.y-b.y)*(a.y-b.y);
        return Math.sqrt(j+k);
    }
    public void populate(){}


}

