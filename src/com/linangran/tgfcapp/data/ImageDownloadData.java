package com.linangran.tgfcapp.data;

import java.io.*;

/**
 * Created by linangran on 30/1/15.
 */
public class ImageDownloadData
{
	public static final String IMAGE_TYPE_JPEG = "jpg";
	public static final String IMAGE_TYPE_GIF = "gif";
	public static final String IMAGE_TYPE_PNG = "png";

	public byte[] imageData;
	public String imageType;

	public ImageDownloadData()
	{
	}

	public void setImageTypeJpeg()
	{
		this.imageType = IMAGE_TYPE_JPEG;
	}

	public void setImageTypePng()
	{
		this.imageType = IMAGE_TYPE_PNG;
	}

	public void setImageTypeGif()
	{
		this.imageType = IMAGE_TYPE_GIF;
	}

	public void loadFromInputStream(InputStream inputStream) throws IOException
	{
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		while (true)
		{
			int count = inputStream.read(buffer);
			if (count == -1)
			{
				break;
			}
			else
			{
				byteArrayOutputStream.write(buffer, 0, count);
			}
		}
		this.imageData = byteArrayOutputStream.toByteArray();
		inputStream.close();
		byteArrayOutputStream.close();
		return;
	}

}
