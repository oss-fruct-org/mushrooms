from PIL import Image
import glob, os

output_dir = "thumbnails"
thumb_size = 180, 180

def createThumbnails():
	if not os.path.exists(output_dir):
		os.makedirs(output_dir)

	for infile in glob.glob("*.jpg"):
		file, ext = os.path.splitext(infile)
		im = Image.open(infile)
		w, h = im.size

		if (w > h):
			delta = w - h
			left = int(delta/2)
			upper = 0
			right = h + left
			lower = h
		else:
			delta = h - w
   			left = 0
   			upper = int(delta/2)
   			right = w
   			lower = w + upper

		im = im.crop((left,upper,right,lower))
		im.thumbnail(thumb_size, Image.ANTIALIAS)
		im.save(output_dir + "/thumb_" + file + ".jpg", "JPEG")

if __name__ == '__main__':
	createThumbnails()