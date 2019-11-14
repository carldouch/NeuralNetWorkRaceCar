public class NeuralNet {
	public Stage stages[];

	public NeuralNet(int stageSizes[]) {
		stages = new Stage[stageSizes.length];
		Stage prev = null;
		for (int i = 0; i < stageSizes.length; i++) {
			stages[i] = new Stage(prev, stageSizes[i]);
			prev = stages[i];
		}
	}
	
	public void loadCoeffs(byte coeffs[]) {
		int idx = 0;
		for (int s = 1; s < stages.length; s++) {
			for (int i = 0; i < stages[s].coeffs.length; i++) {
				for (int j = 0; j < stages[s].coeffs[0].length; j++) {
					stages[s].coeffs[i][j] = coeffs[idx++];
				}
			}
		}
	}
	
	public void loadCoeffsSymmetrical(byte coeffs[]) {
		int idx = 0;
		for (int s = 1; s < stages.length; s++) {
			if (stages[s].coeffs.length % 2 == 1) {
				System.err.println("Symmetrical Net without even sized stages. Bad.");
				return;
			}
			for (int i = 0; i < (stages[s].coeffs.length) / 2; i++) {
				for (int j = 0; j < stages[s].coeffs[0].length; j++) {
					stages[s].coeffs[i][j] = coeffs[idx];
					stages[s].coeffs[stages[s].coeffs.length - 1 - i][stages[s].coeffs[0].length - 1 - j] = coeffs[idx++];
				}
			}
		}
	}
	
	public double[] calc(double input[]) {
		for (int i = 0; i < input.length; i++) {
			stages[0].output[i] = input[i];
		}
		for (int i = 1; i < stages.length; i++) {
			stages[i].calc();
		}
		return stages[stages.length - 1].output;
	}

	public static int calcNumberOfCoeffs(int stageSizes[], boolean symmetrical) {
		int sum = 0;
		if (stageSizes.length < 2)
			return 0;
		for (int i = 1; i < stageSizes.length; i++) {
			if (symmetrical)
				sum += (stageSizes[i] * (stageSizes[i - 1] + 1) + 1) / 2;
			else
				sum += stageSizes[i] * (stageSizes[i - 1] + 1);
		}

		return sum;
	}
}