package core;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import core.SimSettings.SettingsBuilder;
import io.ResultsFileWriter;

public class SimEngine implements Runnable {
	
	private volatile boolean running;

	private SettingsBuilder settingsBuilder;
	private SimSettings settings;

	private ChartParser chartParser;
	private DecisionSolver solver;
	private ResultsProcessor resultsProcessor;
	private Trial[] trials;
	
	private ThreadPoolExecutor executor;

	private double percentComplete;

	public SimEngine(SettingsBuilder settingsBuilder) {
		running = false;
		this.settingsBuilder = settingsBuilder;
		chartParser = new ChartParser(settingsBuilder);
		solver = new DecisionSolver(chartParser);
	}

	private void loadBasicStrategyIntoSolver() {
		chartParser.loadChart(true);
		solver.setSettings(settings);
		solver.loadDecisionTable(true);
	}

	public void loadDeviationsIntoSolver() {
		chartParser.loadChart(false);
		solver.setSettings(settingsBuilder.build());
		solver.loadDecisionTable(false);
	}

	public double getPercentComplete() {
		return percentComplete;
	}
	
	public void resetPercentComplete() {
		percentComplete = 0;
	}

	private void runSimulation() {
		int numThreads = Runtime.getRuntime().availableProcessors();
		settingsBuilder.setNumTrials(5000).setRoundsPerTrial(250000); 

		settings = settingsBuilder.build();
		loadBasicStrategyIntoSolver();

		trials = new Trial[settings.getNumTrials()];
		Runnable[] runnables = new Runnable[settings.getNumTrials()];

		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numThreads);

		for (int i = 0; i < settings.getNumTrials(); i++) {
			trials[i] = new Trial(settings, solver);
			runnables[i] = trials[i];
			executor.execute(runnables[i]);
		}

		executor.shutdown();

		long taskCount = executor.getCompletedTaskCount();
		while (executor.getCompletedTaskCount() < settings.getNumTrials() && running) {		
			if (executor.getCompletedTaskCount() != taskCount) {
				taskCount = executor.getCompletedTaskCount();
				percentComplete = ((double) taskCount / (double) settings.getNumTrials()) * 100;
			}
		}

		taskCount = executor.getCompletedTaskCount();
		percentComplete = ((double) taskCount / (double) settings.getNumTrials()) * 100;

		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(running) {
			resultsProcessor = new ResultsProcessor(settings, trials);
			resultsProcessor.process();
		}
	}

	public String[] getResults() {
		String[] results = { resultsProcessor.getEstimatedEVPerRound(), resultsProcessor.getEstimatedStdDevPerRound(),
				resultsProcessor.getEstimatedN0inRounds(), resultsProcessor.getEstimatedEVPerHour(),
				resultsProcessor.getEstimatedStdDevPerHour(), resultsProcessor.getEstimatedN0inHours(),
				resultsProcessor.getEstimatedROR(), resultsProcessor.getEstimatedHalfROR(), 
				resultsProcessor.getEstimatedQuarterROR() };
		return results;
	}

	public void writeResultsToFile(File file) {
		ResultsFileWriter fileWriter = new ResultsFileWriter(settings, this);
		fileWriter.writeResults(file);
	}

	public DecisionSolver getSolver() {
		return solver;
	}
	
	public void stopEngine() {
		running = false;
		executor.shutdownNow();
		for(int i = 0; i < trials.length; i++) {
			trials[i].stopTrial();
		}
		percentComplete = 0;
	}
	
	public boolean isRunning() {
		return running;
	}

	@Override
	public void run() {
		running = true;
		runSimulation();
	}

}
