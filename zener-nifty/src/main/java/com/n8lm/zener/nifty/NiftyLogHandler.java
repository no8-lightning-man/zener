/*
 * This file is part of Zener.
 *
 * Zener is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or any later version.
 *
 * Zener is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with Zener.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.n8lm.zener.nifty;

import de.lessvoid.nifty.controls.Console;
import de.lessvoid.nifty.tools.Color;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class NiftyLogHandler extends Handler {
	
	private Console console;

	@Override
	public void close() throws SecurityException {
		// TODO Auto-generated method stub

	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub

	}

	@Override
	public void publish(LogRecord record) {
		// ensure that this log record should be logged by this Handler
		if (!isLoggable(record))
			return;
		
		Color color = new Color("#fff");
		
		if (record.getLevel() == Level.WARNING)
			color.fromString("#ff0");
		else if (record.getLevel() == Level.SEVERE)
			color.fromString("#f00");
		else
			color.fromString("#fff");
		
		console.output(this.getFormatter().format(record), color);
	}

	public void setController(Console console) {
		this.console = console;
	}

}
