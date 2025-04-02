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
Selecionar roms pelos critérios (Futuramente os critérios serão customizados):
   - Um game por região respeitando uma ordem pré definida de regiões preferenciais.
   - Priorizando roms que possuam Retroachievements

### Usando o RetroRoms
#### Requisitos:
- Java mínimo 21 instalado.
- Ter última versão do jar baixada.

Navegar até a pasta que se encontra o arquivo **retroRoms.jar** e executar:
`java -jar -o <operacao> -y <sistema> - s <diretorio-origem> -t <diretorio-destino>`

#### Entendendo os parâmetros:
- -o = Tipo da operação que será efetuada conforme explicado em **Extração de roms** e **Seleção de roms**. valores possíveis: **operation** ou **selection**
- -y = Sistema que será processado pela operação, deve se usar nomes conforme tabela **Sistemas/Emuladores suportados** e coluna **Nome Interno**.
- s = Diretório onde se encontram as roms para serem processadas.
- t = Diretório onde as roms processadas serão armazenadas.

#### Exemplo de uso para processamento de um sistema:
- Extração de roms
`java -jar -o extraction -y gb - s /run/media/douglas/Games/Emulation/Packs/Prontos/gb -t /home/douglas/Documents/roms/extraction`

- Seleção de roms
`java -jar -o selection -y gb - s /home/douglas/Documents/roms/extraction/gb -t /home/douglas/Documents/roms/selection`

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
