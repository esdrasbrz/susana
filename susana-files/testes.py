"""
    Script com funcoes para compilar e testar o programa enviado pelo SuSana

    Obs.: Script funciona em python 2.7
"""
import os
import commands

# PATHs usados no SuSana
SUSANA_FILES = "/opt/tomcat/webapps/susana-files/"
#SUSANA_FILES = "/home/esdrasbrz/Projects/java/susana/susana-files/"


# Compila o programa e retorna caso ocorra algum erro ou warning
def compilar(fileName, disciplina, lab):
    # seta o path
    path = SUSANA_FILES + disciplina + "/" + lab + "/"

    return commands.getoutput("gcc -std=c99 -pedantic -Wall -lm %s -o %s" %(path+fileName, path+fileName+"out"))

# Seta as permissoes para o executavel
def set_permissao(fileName, disciplina, lab):
    # seta o path
    path = SUSANA_FILES + disciplina + "/" + lab + "/"

    os.system("chmod a-x %s" %(path+fileName+"out"))
    os.system("chmod u+x %s" %(path+fileName+"out"))

# Executa o programa e compara o arquivo, retornando o resultado
def testar(fileName, disciplina, lab, num):
    # seta os paths
    path = SUSANA_FILES + disciplina + "/" + lab + "/"
    path_testes = path + "testes/"
    path_out = path + "out/"

    # executa e salva a saida. Se o retorno for maior que 100, ele da timeout
    ret = os.system("cd %s && timeout 5 ./%s <%sarq%02d.in >%sarq%02d" %(path, fileName + "out", path_testes, int(num), path_out + fileName, int(num)))

    if (ret > 100):
        return "Timeout!"

    # compara a saida
    diff = commands.getoutput("diff %sarq%02d %sarq%02d.res" %(path_out + fileName, int(num), path_testes, int(num)))

    # apagar o arquivo de saida
    os.system("cd %s && rm %sarq%02d" %(path_out, fileName, int(num)))

    return diff

# Apaga o executavel e o codigo fonte dos arquivos
def limpar(fileName, disciplina, lab):
    # seta o path
    path = SUSANA_FILES + disciplina + "/" + lab + "/"
    # apaga os arquivos
    os.system("cd %s && rm %s && rm %s" %(path, fileName, fileName + "out"))

# cria o diretorio da disciplina
def cria_disciplina(disciplina):
    # realiza o mkdir
    os.system("cd %s && mkdir %s" %(SUSANA_FILES, disciplina))

# cria o diretorio do lab dentro da disciplina correspondente
def cria_lab(disciplina, lab, url_testes, qtd_testes):
    # realia o mkdir
    os.system("cd %s && mkdir %s" %(SUSANA_FILES + disciplina + "/", lab))

    # define o path lab
    path_lab = SUSANA_FILES + disciplina + "/" + lab + "/"

    # cria os diretorios testes e out
    os.system("cd %s && mkdir %s" %(path_lab, "testes"))
    os.system("cd %s && mkdir %s" %(path_lab, "out"))

    # define o path testes
    path_testes = path_lab + "testes/"

    # realiza o download dos arquivos de teste
    for i in range(1, qtd_testes+1):
        os.system("cd %s && curl -O -k %sarq%02d.in" %(path_testes, url_testes, i)) # entrada
    	os.system("cd %s && curl -O -k %sarq%02d.res" %(path_testes, url_testes, i)) # saida
