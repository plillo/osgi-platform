package it.unisalento.idalab.osgi.captcha.rest;

import it.unisalento.idalab.osgi.util.Random;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.amdatu.security.tokenprovider.InvalidTokenException;
import org.amdatu.security.tokenprovider.TokenProvider;
import org.amdatu.security.tokenprovider.TokenProviderException;
import org.amdatu.web.rest.doc.Description;

@Path("captcha/1.0")
@Description("API for Captcha management version 1.0")
public class CaptchaResources {
	private volatile TokenProvider _tokenProvider;
	
	@GET
	@Description("Returns a captcha image")
	@Produces("image/png")
	@Path("{captchaId}")
	public Response getCaptcha(@PathParam("captchaId") String captchaId, @Context HttpServletRequest request) {
		System.out.println("*");
		
		@SuppressWarnings("unchecked")
		Enumeration<String> parNames = request.getParameterNames();
		
		// DEFAULTS
		Color gradiant_start_color = new Color(100, 100, 100); // default start gradiant color
		Color gradiant_end_color = new Color(180, 180, 180); // default end gradiant color
		Color text_color = new Color(200, 200, 200); // default text color
		Color oval_color = null; // default oval color
		int text_length = 5; // default text length
		int width = 300; // default width
		int height = 100; // default height
		int x_gap = 10; // default X gap
		int y_gap = 15; // default Y gap
		int font_size = 25; // default font size
		String font_name = "Courier"; // default font name
		int noise_quantity = 3; // default noise quantity
		int noise_threshold = 50; // default noise threshold
		int pixelate = 0; // default pixelate

		// PARAMETERS
		while (parNames.hasMoreElements()) {
			String name = (String) parNames.nextElement();
			String value = request.getParameter(name);

			// Set gradiant start color
			if (name.equals("gradiant-start-color")) {
				try {
					gradiant_start_color = Color.decode(value);
				} catch (NumberFormatException e) {
				}
				continue;
			}
			// Set gradiant end color
			if (name.equals("gradiant-end-color")) {
				try {
					gradiant_end_color = Color.decode(value);
				} catch (NumberFormatException e) {
				}
				continue;
			}
			// Set text color
			if (name.equals("text-color")) {
				try {
					text_color = Color.decode(value);
				} catch (NumberFormatException e) {
				}
				continue;
			}
			// Set oval color
			if (name.equals("oval-color")) {
				try {
					oval_color = Color.decode(value);
				} catch (NumberFormatException e) {
				}
				continue;
			}
			// Set text length
			if (name.equals("text-length")) {
				try {
					text_length = Integer.parseInt(value);
				} catch (NumberFormatException e) {
				}
				continue;
			}
			// Set width
			if (name.equals("width")) {
				try {
					width = Integer.parseInt(value);
				} catch (NumberFormatException e) {
				}
				continue;
			}
			// Set height
			if (name.equals("height")) {
				try {
					height = Integer.parseInt(value);
				} catch (NumberFormatException e) {
				}
				continue;
			}
			// Set x-gap
			if (name.equals("x-gap")) {
				try {
					x_gap = Integer.parseInt(value);
				} catch (NumberFormatException e) {
				}
				continue;
			}
			// Set height
			if (name.equals("y-gap")) {
				try {
					y_gap = Integer.parseInt(value);
				} catch (NumberFormatException e) {
				}
				continue;
			}
			// Set font name
			if (name.equals("font-name")) {
				try {
					font_name = value;
				} catch (NumberFormatException e) {
				}
				continue;
			}
			// Set font size
			if (name.equals("font-size")) {
				try {
					font_size = Integer.parseInt(value);
				} catch (NumberFormatException e) {
				}
				continue;
			}
			// Set noise quantity
			if (name.equals("noise-quantity")) {
				try {
					noise_quantity = Integer.parseInt(value);
				} catch (NumberFormatException e) {
				}
				continue;
			}
			// Set noise threshold
			if (name.equals("noise-threshold")) {
				try {
					noise_threshold = Integer.parseInt(value);
				} catch (NumberFormatException e) {
				}
				continue;
			}
			// Set noise threshold
			if (name.equals("pixelate")) {
				try {
					pixelate = Integer.parseInt(value);
				} catch (NumberFormatException e) {
				}
				continue;
			}
		}
		
		// CAPTCHA string
		String captcha = Random.getRandomKey(text_length);


		// Set oval color
		if (oval_color == null)
			oval_color = gradiant_start_color;
		
		// CREATE CAPTCHA IMAGE
		// ====================
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();

		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,	RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		g2d.setRenderingHints(rh);
		GradientPaint gp = new GradientPaint(0, 0, gradiant_start_color, 0, height / 2, gradiant_end_color, true);
		g2d.setPaint(gp);
		g2d.fillRect(0, 0, width, height);

		java.util.Random r = new java.util.Random();

		int x = 0, y = 0;

		// random ovals
		for (int i = 0; i < 10; i++) {
			int xc = (Math.abs(r.nextInt()) % width);
			int yc = (Math.abs(r.nextInt()) % height);
			int rc = (Math.abs(r.nextInt()) % height);
			g2d.setColor(oval_color);
			g2d.fillOval(xc - rc, yc - rc, xc + rc, yc + rc);
		}

		// write text
		g2d.setColor(text_color);
		for (int i = 0; i < captcha.length(); i++) {
			Font font = new Font(font_name, Font.BOLD, font_size);
			g2d.setFont(font);
			int i1 = r.nextInt();
			int i2 = r.nextInt();
			x += x_gap + (Math.abs(i1) % font_size);
			y = y_gap + Math.abs(i2) % font_size;

			g2d.drawChars(captcha.toCharArray(), i, 1, x, y);
		}

		// noise
		bufferedImage = ImageTransformationTools.noise(bufferedImage, noise_quantity, noise_threshold);
		if (pixelate > 0)
			bufferedImage = ImageTransformationTools.pixelate(bufferedImage, pixelate);

		// dispose
		g2d.dispose();

		// ====================

		// GET CAPTCHA ID
		/*
		String captchaId = "";
		String uri = request.getRequestURI();
		String context_path = request.getContextPath();
		String pathname = uri.substring(context_path.length());
		Matcher m = Pattern.compile("/(.*).captcha").matcher(pathname);
		if (m.find()) {
			captchaId = m.group(1);
		} else
			captchaId = "captcha";
		*/
		
		
		// SET captcha into the token
		String encryptedToken = _tokenProvider.getTokenFromRequest(request);

		try {
			//SortedMap<String, String> userMap = encryptedToken!=null ? _tokenProvider.verifyToken(encryptedToken) : new TreeMap<String, String>();
			SortedMap<String, String> userMap =  new TreeMap<String, String>();
			userMap.put(captchaId, captcha);
			encryptedToken = _tokenProvider.generateToken(userMap);
		} catch (TokenProviderException e) {
			return Response.serverError().entity("Token Provider Exception").header("Access-Control-Allow-Origin", "*").build();
		}

		// RESPONSE
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bufferedImage, "png", baos);
		} catch (IOException e) {
			return Response.serverError().build();
		}
		byte[] imageData = baos.toByteArray();
		
		System.out.println("captcha: "+captcha);

		return Response.ok(new ByteArrayInputStream(imageData)).cookie(new NewCookie(TokenProvider.TOKEN_COOKIE_NAME, encryptedToken)).build();
	}

	@GET
	@Path("isCaptcha")
	@Description("verifies a captcha value")
	@Produces(MediaType.APPLICATION_JSON)
	public Response isCaptcha(@QueryParam("captchaId") String captchaId, @QueryParam("captchaValue") String captchaValue, @Context HttpServletRequest request) {
		
		Map<String, Object> validation = new TreeMap<String, Object>();
		validation.put("isValid", false);
		
		// GET TOKEN
		String encryptedToken = _tokenProvider.getTokenFromRequest(request);

		try {
			SortedMap<String, String> requestMap = _tokenProvider.verifyToken(encryptedToken);
			if(requestMap.containsKey(captchaId) && requestMap.get(captchaId).equals(captchaValue))
				validation.put("isValid", true);
		} catch (TokenProviderException e) {
			return Response.serverError().entity("Token Provider Exception").header("Access-Control-Allow-Origin", "*").entity(validation).build();
		} catch (InvalidTokenException e) {
			return Response.serverError().entity("Invalid Token Exception").header("Access-Control-Allow-Origin", "*").entity(validation).build();
		}

		// Set message
		validation.put("message", "\""+captchaValue+"\" for captcha ID \""+captchaId+"\" is "+((boolean)validation.get("isValid")?"":"not ")+"a valid captcha");
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(validation).build();
	}

}
