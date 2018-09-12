# EmployBox WebApp

Na aplicação web de forma a evitar o carregamento de páginas inteiras a cada pedido. Foi decidido desenvolver a aplicação web como uma Single Page Application. Para facilitar o desenvolvimento de uma SPA foi escolhido usar a biblioteca de javascript React, por ser facil de criar User interfaces interativas. De modo a facilitar a navegação pela web app e permitir bookmarking foi usado a biblioteca React Router.

Para tornar UIs mais apelativas foi usado a biblioteca de css bootstrap.
Como o bootstrap é responsive entao os componentes conseguem se adaptar as varias resoluçoes de ecra permitindo assim correr tanto em Desktop como em dispositivos moveis

# Componentes

Cada componente react é composto pela sua lógica e a sua renderização.
Como podemos ver na figura ao lado, temos 4 componentes ListForm e cada um deles contém uma lista e um Generic Form. Como se pode verificar o GenericForm tem diferentes inputs pois é possível através das propriedades do componente especificar os seus inputs. Com isto também é possível notar que os componentes são reutilizaveis

# Rapper

* Biblioteca Java que se enquadra no ambito das plataformas ORM(Object Relational Mapping).
* Requere uma driver JDBC.
* O rapper tem como função o mapeamento entre o modelo de objectos de dominio e o modelo relacional, abstraindo assim a criação de comandos sql para efetuar as operações CRUD para manusear os dados presentes na base de dados.
* De modo a permitir que haja acessos concorrentes aos mesmos dados da base de dados o rapper tem suporte transacional através do UnitOfWork
* O rapper possuí uma interface assíncrona porém não bloqueante devido ao JDBC não possuir uma interface não bloqueante, de forma a ultrapassar este problema foi decidido integrar no rapper a biblioteca Vert.x que possuí uma thread pool que está destinada a operações bloqueantes. Permitindo assim um melhor uso dos recursos.

# Decisões

No desenvolvimento do rapper foi seguido padrões de desenho presentes no livro de Martin Fowler Patterns of Enterprise Application Architecture como: 
  * Unit Of Work
  * Data Mapper
  * Identity Map
  * Repository 

Este último (Repository) acabou por ser descontinuado pois apenas tinha como objectivo registar as alterações aos objectos de dominio no Unit Of Work. Visto que estas operações podiam ser facilmente realizadas no DataMapper foi decidido fundir o Repository e o DataMapper num só.
Ficando assim apenas com DataMapper

Outra situação em que acabou também por ser descontinuado foi a "Atualização de referncias das listas" pois inicialmente os campos que representavam relações entre outras tabelas eram assíncronas sendo do tipo CompletableFuture<List> mas devido a otimizações acabou por passar a ser Lazy onde só é carregado se o utilizador necessitar, Passando a ser do tipo Supplier<CompletableFuture<List>>. Mas com esta abordagem se o utilizador chamasse esta função quando já tivesse fechado a transação iriam acabar por ficar conexões abertas sendo assim este campo passou a ser do tipo Function<UnitOfWork, CompletableFuture<List>>.

Durante o desenvolvimento da biblioteca Rapper, houve vários pontos de decisão que levaram a uma reformatação do código da web API.

# Exemplo

Aqui podemos ver um exemplo de código da estrutura final do Rapper. 
Cada vez que se pretende iniciar uma transação é nessessário criar um UnitOfWork. De seguida, para obter uma instancia de DataMapper para poder efetuar operações com a base de dados, deve-se a instância a partir do método estático getMapper presente no MapperRegistry (que mais à frente vamos ver porquê). Por fim, qualquer método do DataMapper retorna um CompletableFuture onde se deve colocar uma continuação para fechar a transação, se o commit por alguma razão correr mal será feito rollback.

# Estrutura

Nesta figura, pode-se observar a interação dos componnetes principais do rapper. o MapperRegistry fornece instâncias de DataMapper. Para instanciar o DataMapper é necessário uma instância de UnitOfWork, especificada pelo utilizador(como obeservado no slide anterior), uma instancia de MapperSettings e ExternalsHandler, que são criadas automaticamente pelo MapperRegistry, e é apenas criada uma instância por cada tipo de DomainObject e o MapperRegistry é responsável por fazer cache, embrulhando estes no tipo Container.

# Domain Object

Domain Object é uma interface que deve ser implementada por todos os objectos de domínio que representam uma tabela da base de dados. Implementando estes dois métodos getIdentityKey e getVersion, getIdentityKey retorna a chave primária e o getVersion, caso o objecto possua controlo de versão deve retornar o valor da versão, caso contrário pode retornar por exemplo 0,
pois não é usado.
O nome da classe deve ter o mesmo nome da tabela e os campos devem ter o mesmo nome das colunas da tabela.

# Controlo de Versões

Um Domain Object opcionalmente pode suportar controlo de versões de modo a manter a coesão dos dados em memória e os dados presentes na base de dados. De modo a suportar isto, o Domain Object deverá ter um campo que representa a sua versão, anotado com @Version. Esta coluna é auto-incrementada pela base de dados (trigger ou rowversion sql-server) a cada escrita que haja na sua entrada da tabela.
Assim poderemos estabelecer que as escritas na base de dados só terão sucesso caso tenhamos, em memória, a versão que se encontra na base de dados.
Ao concluir com sucesso a escrita na base de dados este campo vai será atualizado com o novo valor. 

# Diagrama de fluxo num caso de uso de versão


# Chaves primarias

Caso seja uma chave simples o campo deve ser anotado com @Id e caso a chave seja auto-incrementada pela base de dados deve ser passado o parametro isIdentity a true, caso contrario pode ser passado false ou não passar pois o seu valor default é false.
Caso seja uma chave composta o campo deve ser anotado com @EmbeddedID e ser uma instancia de uma classe em que os seus campos sejam as colunas que representam as chaves primarias. Esta classe também tem de estender de EmbeddedIDClass, de modo a comparar os objetos pelo valor das chaves.

# Relações com outras tabelas

Os campos que representam relações com outras tabelas devem ser anotados com @ColumName.
Os campos devem ser do tipo Function<UnitOfWork, CompletableFuture<T>>, caso referencie múltiplos valores ou Foreign<T> caso referencie apenas um valor.

#  MapperSettings

A classe MapperSettings é responsável por dividir os campos em 4 grupos:
* SqlField - é o tipo principal, todos os outros estendem deste. Representa uma coluna normal
* SqlFieldId - representa as chaves primárias, ou seja os campos anotados com @Id ou @EmbeddedId
* SqlFieldVersion - representa a coluna correspondente à versao, anotado com @Version
* SQLFieldExternal - representa os campos que representam as relações com outras tabelas, anotado com @ColumName

Separados os campos devidamente, esta classe construirá as query strings que permitem efetuar as ações CRUD.

Todo este processo tem um peso computacional elevado devido às chamdas através de introspeção, por isso este processo acontece todo na construção de uma instancia de MapperSettings e essa instancia é guardada em MapperRegistry havendo assim apenas uma instancia de MapperSettings por cada tipo de DomainObject no tempo de vida da aplicação.



