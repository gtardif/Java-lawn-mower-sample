package gtardif.mowitnow;

import static com.google.common.collect.Lists.*;
import static gtardif.mowitnow.Command.*;
import static gtardif.mowitnow.Direction.*;
import static org.fest.assertions.Assertions.*;
import static org.unitils.reflectionassert.ReflectionAssert.*;
import java.util.ArrayList;
import org.junit.Test;
import com.google.common.collect.Lists;

public class MowerTest {
	private static final ArrayList<Command> EMPTY_COMMANDS = Lists.<Command> newArrayList();

	@Test
	public void canInitMower() {
		Mower mower = new Mower(3, 4, WEST, newArrayList(RIGHT, ADVANCE, LEFT, ADVANCE, ADVANCE), 10, 10);

		assertThat(mower.getX()).isEqualTo(3);
		assertThat(mower.getY()).isEqualTo(4);
		assertThat(mower.getDirection()).isEqualTo(WEST);
		assertThat(mower.getCommands()).containsExactly(RIGHT, ADVANCE, LEFT, ADVANCE, ADVANCE);
	}

	@Test
	public void canReadMowerConfig() {
		Mower mower = Mower.readConfig("1 2 N", "GADAAGA", 10, 11);

		assertReflectionEquals(mower, new Mower(1, 2, NORTH, newArrayList(LEFT, ADVANCE, RIGHT, ADVANCE, ADVANCE, LEFT, ADVANCE), 10, 11));
	}

	@Test
	public void canChangeDirection() {
		Mower mower = new Mower(3, 4, NORTH, newArrayList(RIGHT, ADVANCE, LEFT), 10, 10);
		mower.step();

		assertReflectionEquals(mower, new Mower(3, 4, EAST, newArrayList(ADVANCE, LEFT), 10, 10));
	}

	@Test
	public void canFindCorrectDirection() {
		assertThat(Mower.getNewDirection(NORTH, RIGHT)).isEqualTo(EAST);
		assertThat(Mower.getNewDirection(EAST, RIGHT)).isEqualTo(SOUTH);
		assertThat(Mower.getNewDirection(SOUTH, RIGHT)).isEqualTo(WEST);
		assertThat(Mower.getNewDirection(WEST, RIGHT)).isEqualTo(NORTH);

		assertThat(Mower.getNewDirection(NORTH, LEFT)).isEqualTo(WEST);
		assertThat(Mower.getNewDirection(WEST, LEFT)).isEqualTo(SOUTH);
		assertThat(Mower.getNewDirection(SOUTH, LEFT)).isEqualTo(EAST);
		assertThat(Mower.getNewDirection(EAST, LEFT)).isEqualTo(NORTH);
	}

	@Test
	public void canAdvanceOneStep() {
		Mower mower = new Mower(3, 4, NORTH, newArrayList(ADVANCE, LEFT, ADVANCE), 10, 10);
		mower.step();

		assertReflectionEquals(mower, new Mower(3, 5, NORTH, newArrayList(LEFT, ADVANCE), 10, 10));
	}

	@Test
	public void canIgnoreCommandIfGoingTooFarNorth() {
		Mower mower = new Mower(3, 4, NORTH, newArrayList(ADVANCE, LEFT, ADVANCE), 4, 4);
		mower.step();

		assertReflectionEquals(mower, new Mower(3, 4, NORTH, newArrayList(LEFT, ADVANCE), 4, 4));
	}

	@Test
	public void canIgnoreCommandIfGoingTooFarSouth() {
		Mower mower = new Mower(3, 0, SOUTH, newArrayList(ADVANCE, LEFT, ADVANCE), 4, 4);
		mower.step();

		assertReflectionEquals(mower, new Mower(3, 0, SOUTH, newArrayList(LEFT, ADVANCE), 4, 4));
	}

	@Test
	public void canIgnoreCommandIfGoingTooFarEast() {
		Mower mower = new Mower(4, 0, EAST, newArrayList(ADVANCE, LEFT, ADVANCE), 4, 4);
		mower.step();

		assertReflectionEquals(mower, new Mower(4, 0, EAST, newArrayList(LEFT, ADVANCE), 4, 4));
	}

	@Test
	public void canIgnoreCommandIfGoingTooFarWest() {
		Mower mower = new Mower(0, 0, WEST, newArrayList(ADVANCE, LEFT, ADVANCE), 4, 4);
		mower.step();

		assertReflectionEquals(mower, new Mower(0, 0, WEST, newArrayList(LEFT, ADVANCE), 4, 4));
	}

	@Test
	public void canExecuteAllCommands() {
		Mower mower = new Mower(3, 4, NORTH, newArrayList(ADVANCE, LEFT, ADVANCE), 10, 10);
		mower.mow();

		assertReflectionEquals(mower, new Mower(2, 5, WEST, EMPTY_COMMANDS, 10, 10));
	}

	@Test(expected = IllegalArgumentException.class)
	public void throwsExceptionIfMowerContainsInvalidCommands() {
		Mower.readConfig("1 2 N", "GATACA", 10, 11);
	}

	@Test(expected = IllegalArgumentException.class)
	public void throwsExceptionIfMowerContainsInvalidPosition() {
		Mower.readConfig("1 2 3 N", "", 10, 11);
	}
}
