# RetroRoms - Organização de roms de diversos emuladores.

Este projeto tem por finalidade organizar roms de diversos emuladores baseando:
- Na base de dados do NoIntro.
- Na convenção de nomes de sistemas(emuladores) e nas extensões de roms usadas pelo Batocera.
- No sistema de conquistas do Retroachievements.
*De ante mão, meu muito obrigado a estes 3 projetos incríveis.*

O RetroRoms possui as seguintes funções:

## Extração de roms
Extrai roms válidas[^1] de arquivos compactados e pastas e subpastas. Tipo de arquivos compactados que são suportados: **zip** e **7z**


## Seleção de roms
Selecionar roms pelos critérios estabelecidos no [Arquivo de Configuração](https://github.com/Roknauta/retroRoms/edit/master/README.md#arquivo-de-configura%C3%A7%C3%A3o)

### Arquivo de Configuração
O arquivo de configuração que pode ser baixado em: **config/config.properties** é usado para determinar os parâmetros da execução de todas as funções disponíveis, é importante ler a descrição de cada parâmetro para evitar erros durante a execução.

### Usando o RetroRoms
#### Requisitos:
- Java mínimo 21 instalado.
- Ter última versão do jar baixada.
- Baixar o arquivo **config.properties** na pasta **config**.

Após baixar o [Arquivo de Configuração](https://github.com/Roknauta/retroRoms/edit/master/README.md#arquivo-de-configura%C3%A7%C3%A3o), abra-o e preencha cada uma dos parâmetros. Cada parâmetro tem um comentário explicativo que se inicia com o caractere `#`. Leia atentamente a cada parâmetro pois eles definem o resultado final.

**IMPORTANTE**: No parâmetro `geral.diretorio.origem`, deve ser informado sempre a raiz de onde estaõ os arquivos de origem, e dentro desta pasta os arquivos devem estar dentro de pastas correspondentes aos emuladores conforme nome usado em [Sistemas/Emuladores suportados](https://github.com/Roknauta/retroRoms?tab=readme-ov-file#sistemasemuladores-suportados)

Exemplo de uso no arquivo config.properties:</br>
`geral.operacao=extracao` </br>
`geral.sistemas=gb,gbc`</br>
`geral.diretorio.origem=/run/media/douglas/Games/Emulation/Packs/Prontos`

Notar que como vou processar 2 sistemas (gb, gbc), no diretório **/run/media/douglas/Games/Emulation/Packs/Prontos** devem existir as pastas **gb** e **gbc** com o conteúdo dentro das respectivas pastas.

Para executar, basta abrir o terminal (cmd no Windows) e executar: 
`java -jar retroRoms.jar <caminho_carquivo_de_configuração>`

### Sistemas/Emuladores suportados

Abaixo a lista dos sistemas que são suportados pelo RetroRoms:
| Sistema       | Nome Interno |
| ------------- | ------------- |
| Atari 2600    | atari2600  |
| Atari 5200    | atari5200  |
| Atari 7800    | atari7800  |
| Commodore 64    | c64  |
| Channel F    | channelf  |
| ColecoVision    | colecovision  |
| Game Gear     | gamegear  |
| Game Boy    | gb  |
| Game Boy Advance    | gba  |
| Game Boy Color    | gbc  |
| Intellivision    | intellivision  |
| Jaguar    | jaguar  |
| Lynx    | lynx  |
| Master System    | mastersystem  |
| Mega Drive/Genesis    | megadrive  |
| Nintendo 64    | n64  |
| Nintendo Entertainment System (NES) | nes  |
| Super Nintendo Entertainment System (SNES)    | snes  |
| Magnavox Odyssey²    | o2em  |
| TurboGrafx-16/PC Engine    | pcengine  |
| Sega 32X    | snes  |

A coluna **Nome Interno** é importante pois ela é usada para organização de pastas nos processos que o RetroRoms opera.

[^1]: Roms válidas são baseadas nas suas extensões conforme documentado no Batocera.
