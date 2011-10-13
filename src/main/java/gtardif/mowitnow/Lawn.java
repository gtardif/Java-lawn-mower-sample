package gtardif.mowitnow;

import static org.apache.commons.lang.StringUtils.*;
import java.util.List;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class Lawn {
	private final int height;
	private final int width;
	private final List<Mower> mowers;

	public Lawn(int width, int height, List<Mower> mowers) {
		this.height = height;
		this.width = width;
		this.mowers = mowers;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public List<Mower> getMowers() {
		return mowers;
	}

	public static Lawn readConfig(String config) {
		String[] lines = split(config, '\n');
		Preconditions.checkArgument((lines.length % 2) == 1, "lawn configuration must one line for lawn dimensions plus 2 lines per mower");
		Preconditions.checkArgument(lines[0].matches("[0-9]* [0-9]*"), "lawn dimension must contain only width and height values");
		String[] xy = split(lines[0], ' ');
		int width = Integer.parseInt(xy[0]);
		int height = Integer.parseInt(xy[1]);
		int nbMower = (lines.length - 1) / 2;
		List<Mower> mowers = Lists.newArrayList();
		for (int i = 0; i < nbMower; i++) {
			mowers.add(Mower.readConfig(lines[(2 * i) + 1], lines[(2 * i) + 2], width, height));
		}
		return new Lawn(width, height, mowers);
	}

	public void mow() {
		for (Mower mower : mowers) {
			mower.mow();
			System.out.println(mower.getX() + " " + mower.getY() + " " + mower.getDirection().name().charAt(0));
		}
	}
}
