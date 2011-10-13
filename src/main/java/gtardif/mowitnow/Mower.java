package gtardif.mowitnow;

import static com.google.common.base.Preconditions.*;
import static com.google.common.collect.Lists.*;
import static gtardif.mowitnow.Command.*;
import static gtardif.mowitnow.Direction.*;
import static java.lang.Integer.*;
import static org.apache.commons.lang.StringUtils.*;
import java.util.List;
import java.util.Map;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class Mower {
	private static final Map<String, Direction> DIRECTIONS_BY_NAME = ImmutableMap.of("N", NORTH, "S", SOUTH, "E", EAST, "W", WEST);
	private static final Map<Character, Command> COMMANDS_BY_NAME = ImmutableMap.of('A', ADVANCE, 'G', LEFT, 'D', RIGHT);
	private static final Map<Direction, Direction> TURN_RIGHT = ImmutableMap.of(NORTH, EAST, EAST, SOUTH, SOUTH, WEST, WEST, NORTH);
	private static final Map<Direction, Direction> TURN_LEFT = ImmutableMap.of(NORTH, WEST, WEST, SOUTH, SOUTH, EAST, EAST, NORTH);

	private int x;
	private int y;
	private Direction direction;

	private final List<Command> commands;
	private final int lawnWidth;
	private final int lawnHeight;

	public Mower(int x, int y, Direction direction, List<Command> commands, int lawnWidth, int lawnHeight) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.commands = commands;
		this.lawnWidth = lawnWidth;
		this.lawnHeight = lawnHeight;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Direction getDirection() {
		return direction;
	}

	@VisibleForTesting
	List<Command> getCommands() {
		return ImmutableList.copyOf(commands);
	}

	public static Mower readConfig(String position, String commandChars, int lawnWidth, int lawnHeight) {
		checkArgument(position.matches("[0-9]* [0-9]* [NEWS]"), "mower position must be of the form X Y Direction");
		checkArgument(commandChars.matches("[GDA]*"), "Commands must be one of G, D, A");
		String[] config = split(position, ' ');
		List<Command> commands = newArrayList();
		for (char c : commandChars.toCharArray()) {
			commands.add(COMMANDS_BY_NAME.get(c));
		}
		return new Mower(parseInt(config[0]), parseInt(config[1]), DIRECTIONS_BY_NAME.get(config[2]), commands, lawnWidth, lawnHeight);
	}

	public void step() {
		if (commands.isEmpty()) {
			return;
		}
		Command command = commands.get(0);
		switch (command) {
			case ADVANCE:
				advance();
				break;
			case RIGHT:
			case LEFT:
				direction = getNewDirection(direction, command);
				break;
		}
		commands.remove(0);
	}

	@VisibleForTesting
	static Direction getNewDirection(Direction direction, Command side) {
		switch (side) {
			case LEFT:
				return TURN_LEFT.get(direction);
			case RIGHT:
				return TURN_RIGHT.get(direction);
			default:
				throw new RuntimeException();
		}
	}

	private void advance() {
		switch (direction) {
			case NORTH:
				if (y < lawnHeight) {
					y = y + 1;
				}
				break;
			case EAST:
				if (x < lawnWidth) {
					x = x + 1;
				}
				break;
			case SOUTH:
				if (y > 0) {
					y = y - 1;
				}
				break;
			case WEST:
				if (x > 0) {
					x = x - 1;
				}
				break;
		}
	}

	public void mow() {
		while (!commands.isEmpty()) {
			step();
		}
	}
}
