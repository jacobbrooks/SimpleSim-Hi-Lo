package core;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class ResultsProcessor {

	private SimSettings settings;

	private Trial[] trials;

	private double estimatedEVPerRound;
	private double estimatedStdDevPerRound;
	private double estimatedN0inRounds;
	
	private double estimatedEVPerHour;
	private double estimatedStdDevPerHour;
	private double estimatedN0inHours;

	private double estimatedROR;
	private double estimatedQuarterROR;
	private double estimatedHalfROR;
	
	private final int hoursPerTrial;

	public ResultsProcessor(SimSettings settings, Trial[] trials) {
		this.settings = settings;
		this.trials = trials;
		hoursPerTrial = (int) Math.floor(settings.getRoundsPerTrial() / settings.getRoundsPerHour());
	}

	public void process() {
		estimatedEVPerRound = calcEV(settings.getRoundsPerTrial());
		estimatedStdDevPerRound = calcStDev(settings.getRoundsPerTrial());
		estimatedN0inRounds = calcN0(settings.getRoundsPerTrial());
		
		estimatedEVPerHour = calcEV(hoursPerTrial);
		estimatedStdDevPerHour = calcStDev(hoursPerTrial);
		estimatedN0inHours = calcN0(hoursPerTrial);
		
		estimatedROR = calcROR(0);
		estimatedQuarterROR = calcROR(1);
		estimatedHalfROR = calcROR(2);		
	}

	private double getAvgTotalEV() {
		double sumEV = 0;
		for (int i = 0; i < settings.getNumTrials(); i++) {
			sumEV += trials[i].getStats().getNetProfit();
		}
		return sumEV / settings.getNumTrials();
	}
	
	private double getAvgTotalVariance() {
		double totalAvgEV = getAvgTotalEV();
		double sumSquaredDiffs = 0;
		for(int i = 0; i < settings.getNumTrials(); i++) {
			double totalEV = trials[i].getStats().getNetProfit();
			double diff = totalEV - totalAvgEV;
			sumSquaredDiffs += (diff * diff);
		}
		return sumSquaredDiffs / settings.getNumTrials();
	}
	
	
	private double calcEV(double divisor) {
		double totalEV = getAvgTotalEV();
		double evPerUnit = totalEV / divisor;
		return evPerUnit;
	}
	
	private double calcStDev(double divisor) {
		double variancePerUnit = getAvgTotalVariance() / divisor;
		double stDevPerUnit = Math.sqrt(variancePerUnit);
		return stDevPerUnit;
	}
	
	private double calcN0(double divisor) {
		double variance = getAvgTotalVariance() / divisor;
		double EV = getAvgTotalEV() / divisor;
		return variance / (EV * EV);
	}

	private double calcROR(int rorMappingIndex) {
		double sumROR = 0;
		for (int i = 0; i < settings.getNumTrials(); i++) {
			if (getMappingROR(trials[i], rorMappingIndex)) {
				sumROR++;
			}
		}
		double result = (sumROR / (double) settings.getNumTrials()) * 100;
		return result;
	}

	private boolean getMappingROR(Trial t, int i) {
		boolean[] mapping = { t.getStats().isBankrollRuined(), t.getStats().isBankrollRuined25p(),
				t.getStats().isBankrollRuined50p() };
		return mapping[i];
	}

	private String toFormattedString(double stat) {
		DecimalFormat df = new DecimalFormat("#.####");
		df.setRoundingMode(RoundingMode.HALF_UP);
		String statString = df.format(stat);
		return statString;
	}

	public String getEstimatedEVPerRound() {
		return toFormattedString(estimatedEVPerRound);
	}
	
	public String getEstimatedStdDevPerRound() {
		return toFormattedString(estimatedStdDevPerRound);
	}
	
	public String getEstimatedN0inRounds() {
		return toFormattedString(estimatedN0inRounds);
	}
	
	public String getEstimatedEVPerHour() {
		return toFormattedString(estimatedEVPerHour);
	}
	
	public String getEstimatedStdDevPerHour() {
		return toFormattedString(estimatedStdDevPerHour);
	}
	
	public String getEstimatedN0inHours() {
		return toFormattedString(estimatedN0inHours);
	}

	public String getEstimatedROR() {
		return toFormattedString(estimatedROR);
	}

	public String getEstimatedQuarterROR() {
		return toFormattedString(estimatedQuarterROR);
	}

	public String getEstimatedHalfROR() {
		return toFormattedString(estimatedHalfROR);
	}

}
