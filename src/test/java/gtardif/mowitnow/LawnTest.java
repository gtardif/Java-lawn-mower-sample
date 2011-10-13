package gtardif.mowitnow;

import static gtardif.mowitnow.Direction.*;
import static org.fest.assertions.Assertions.*;
import org.junit.Test;

public class LawnTest {
	@Test
	public void canInitLawn() {
		Lawn lawn = new Lawn(7, 5, null);

		assertThat(lawn.getHeight()).isEqualTo(5);
		assertThat(lawn.getWidth()).isEqualTo(7);
	}

	@Test
	public void canReadLawnConfig() {
		Lawn lawn = Lawn.readConfig("5 6");

		assertThat(lawn.getWidth()).isEqualTo(5);
		assertThat(lawn.getHeight()).isEqualTo(6);
	}

	@Test(expected = IllegalArgumentException.class)
	public void canThrowExceptionIfLawnConfigHasZeroLines() {
		Lawn.readConfig("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void canThrowExceptionIfLawnConfigDoesNotHave2LinesPerMower() {
		Lawn.readConfig("5 6\n1 2 N\nGAAG\n1 3 E");
	}

	@Test(expected = IllegalArgumentException.class)
	public void canThrowExceptionIfLawnDimensionsHasSyntaxErrors() {
		Lawn.readConfig("5 6E");
	}

	@Test(expected = IllegalArgumentException.class)
	public void canThrowExceptionIfLawnDimensionsHasTooManyData() {
		Lawn.readConfig("5 6 7");
	}

	@Test(expected = IllegalArgumentException.class)
	public void canThrowExceptionIfLawnDimensionsHasOnlyOneValue() {
		Lawn.readConfig("7");
	}

	@Test
	public void canAddOneMower() {
		Lawn lawn = Lawn.readConfig("5 5\n" + "1 2 N\n" + "GAGAGAGAA");

		assertThat(lawn.getMowers()).hasSize(1);
	}

	@Test
	public void canAddMowers() {
		Lawn lawn = Lawn.readConfig("5 5\n" + "1 2 N\n" + "GAGAGAGAA\n" + "3 3 E\n" + "AADAADADDA");

		assertThat(lawn.getMowers()).hasSize(2);
	}

	@Test
	public void canExecuteMowing() {
		Lawn lawn = Lawn.readConfig("5 5\n" + "1 2 N\n" + "GAGAGAGAA\n" + "3 3 E\n" + "AADAADADDA");
		lawn.mow();

		assertThat(lawn.getMowers()).hasSize(2);

		assertThat(lawn.getMowers().get(0).getX()).isEqualTo(1);
		assertThat(lawn.getMowers().get(0).getY()).isEqualTo(3);
		assertThat(lawn.getMowers().get(0).getDirection()).isEqualTo(NORTH);

		assertThat(lawn.getMowers().get(1).getX()).isEqualTo(5);
		assertThat(lawn.getMowers().get(1).getY()).isEqualTo(1);
		assertThat(lawn.getMowers().get(1).getDirection()).isEqualTo(EAST);
	}
}
