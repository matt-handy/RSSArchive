package handy.rssarchive.html.test;

import static org.junit.Assert.*;

import org.junit.Test;

import handy.rssarchive.html.BasicTagCleaner;
import handy.rssarchive.html.ImageTagCleaner;

public class ImageTagCleanerTest {

	@Test
	public void test() {
		String testData = "Authored by Mac Slavo via SHTFplan.com, Regulatory powers have just been dangerously weaponized against the National Rifle Association.New York Governor Andrew Cuomo recent directiveÂ to financial regulatorsÂ wants them to pressure private companies to break ties with the NRA, â€œor elseâ€¦â€� <img data-entity-type=\"file\" data-entity-uuid=\"76ee08ac-8f97-4e9a-81e2-9c98b146745c\" data-responsive-image-style=\"inline_images\" height=\"292\" width=\"500\" srcset=\"https://www.zerohedge.com/sites/default/files/styles/inline_image_desktop/public/inline-images/cumono.jpg?itok=cu3Y8ZYE 1x\" src=\"https://www.zerohedge.com/sites/default/files/inline-images/cumono.jpg\" alt=\"\" typeof=\"foaf:Image\" /> New York has never been known as a basti<img data-entity-type=\"file\" data-entity-uuid=\"76ee08ac-8f97-4e9a-81e2-9c98b146745c\" data-responsive-image-style=\"inline_images\" height=\"292\" width=\"500\" srcset=\"https://www.zerohedge.com/sites/default/files/styles/inline_image_desktop/public/inline-images/cumono.jpg?itok=cu3Y8ZYE 1x\" src=\"https://www.zerohedge.com/sites/default/files/inline-images/cumono.jpg\" alt=\"\" typeof=\"foaf:Image\" />";
		String imgClean = ImageTagCleaner.imageTagCleaner(testData, "C:\\Users\\The Constant Admin\\Desktop\\TestLander");
		String expected = "Authored by Mac Slavo via SHTFplan.com, Regulatory powers have just been dangerously weaponized against the National Rifle Association.New York Governor Andrew Cuomo recent directiveÂ to financial regulatorsÂ wants them to pressure private companies to break ties with the NRA, â€œor elseâ€¦â€� " + System.getProperty("line.separator") + "See Image #1" + System.getProperty("line.separator") + " New York has never been known as a basti" + System.getProperty("line.separator") + "See Image #2" + System.getProperty("line.separator");
		assertEquals(imgClean, expected);
	}

}
