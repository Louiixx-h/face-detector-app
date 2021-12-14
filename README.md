# Detector de Faces

<!---Esses são exemplos. Veja https://shields.io para outras pessoas ou para personalizar este conjunto de escudos. Você pode querer incluir dependências, status do projeto e informações de licença aqui--->

![GitHub repo size](https://img.shields.io/github/repo-size/iuricode/README-template?style=for-the-badge)
![GitHub language count](https://img.shields.io/github/languages/count/Louiixx-h/Detector-de-Faces?style=for-the-badge)
![GitHub forks](https://img.shields.io/github/forks/Louiixx-h/Detector-de-Faces?style=for-the-badge)
![Bitbucket open issues](https://img.shields.io/bitbucket/issues/iuricode/README-template?style=for-the-badge)
![Bitbucket open pull requests](https://img.shields.io/bitbucket/pr-raw/iuricode/README-template?style=for-the-badge)

## Aplicativo android para detecção de rostos em imagens.

Este aplicativo faz uso do [ML Kit do Android](https://developers.google.com/ml-kit/vision/face-detection/android).

## Principais funcionalidades

- **Destaques de faces:** Obtenha os contornos dos rostos.
- **Reconhecer e localizar características faciais:** Obter as coordenadas dos olhos, orelhas, bochechas, nariz e boca de cada rosto detectado.
- **Reconhecer expressões faciais:** Determine se uma pessoa está sorrindo ou está de olhos fechados.

## Observções

Para reconhecimento facial, você deve usar uma imagem com dimensões de pelo menos 480x360 pixels. Para que o ML Kit detecte com precisão rostos, as imagens de entrada devem conter rostos representados por dados de pixels suficientes. Em geral, cada rosto que você deseja detectar em uma imagem deve ter pelo menos 100x100 pixels. Se você quiser detectar os contornos dos rostos, o Kit ML requer uma entrada de maior resolução: cada rosto deve ter pelo menos 200x200 pixels.

A orientação de um rosto em relação à câmera também pode afetar quais características faciais o Kit ML detecta.
