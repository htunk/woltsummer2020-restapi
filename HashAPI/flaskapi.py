from flask import Flask, json, request
import requests
import blurhash

api = Flask(__name__)

@api.route('/hash', methods=['GET'])
def generate_hash():
  url = str(request.args.get('url'))
  x_components = int(request.args.get('x_comp'))
  y_components = int(request.args.get('y_comp'))
  image = requests.get(url, stream=True)
  return blurhash.encode(image.raw, x_components=x_components, y_components=y_components)

if __name__ == '__main__':
    api.run(host='0.0.0.0')