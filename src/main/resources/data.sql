-- se precisar resetar a tabela (meu docker deu problema DE NOVO)

-- roda esses comandos:
-- docker cp src/main/resources/data.sql marcapasso3d-db:/data.sql
-- docker exec marcapasso3d-db psql -U admin -d marcapasso3d -f /data.sql

-- VERIFIQUE A TABELA DE CEPS::
CREATE TABLE IF NOT EXISTS ceps (
    id_cep              SERIAL PRIMARY KEY,
    cep                 VARCHAR(9)     NOT NULL UNIQUE,
    cidade              VARCHAR(255)   NOT NULL,
    preco               NUMERIC(10, 2) NOT NULL,
    prazo_entrega_dias  INTEGER        NOT NULL,
    criado_em           TIMESTAMP,
    atualizado_em       TIMESTAMP
);
 
INSERT INTO ceps (cep, cidade, preco, prazo_entrega_dias, criado_em, atualizado_em) VALUES
    ('55535000', 'Joaquim Nabuco - PE', 25.00, 5, NOW(), NOW()),
    ('51160220', 'Recife - PE',         15.00, 7, NOW(), NOW()),
    ('55578000', 'Amendaraé - PE',      30.00, 6, NOW(), NOW()),
    ('55520330', 'Ribeirão - PE',       25.00, 5, NOW(), NOW()),
    ('55540000', 'Palmares - PE',       15.00, 7, NOW(), NOW()),
    ('55550000', 'Água Preta - PE',     30.00, 6, NOW(), NOW())
ON CONFLICT (cep) DO NOTHING;

TRUNCATE TABLE produtos RESTART IDENTITY CASCADE;

INSERT INTO produtos (nome, descricao, resumo_ia, preco, imagem_principal, personalizavel, categoria, material, estoque, total_vendas) VALUES

('Luminária 3D',
 'Luminária personalizada em formato de lua, feita em impressão 3D com luz LED interna suave.',
 'Ilumine seu ambiente com a Luminária 3D em formato de lua, feita com PLA biodegradável e luz LED suave, unindo sustentabilidade e design moderno. Por apenas R$ 89,90, transforme sua decoração com um toque único e ecológico. Compre agora e dê vida ao seu espaço!',
 89.90, '/imagem-produtos/luminaria3d.jpg', true, 'Decoração', 'PLA Biodegradável', 15, 0),

('Abajur Cogumelo',
 'Abajur decorativo no formato de cogumelo, impresso em 3D com acabamento fosco e iluminação interna suave.',
 'Dê um charme especial ao seu cantinho com o Abajur Cogumelo, uma peça única impressa em 3D com design encantador e iluminação aconchegante. Feito em PLA de alta qualidade, ele combina funcionalidade e decoração em um só produto. Adquira o seu e transforme qualquer ambiente!',
 74.90, '/imagem-produtos/abajurcogumelo.jpg', true, 'Decoração', 'PLA', 10, 0),

('Luminária LED Colorida',
 'Luminária 3D com efeito de luz colorida, ideal para decoração de quartos e salas gamer.',
 'Crie uma atmosfera incrível com a Luminária LED Colorida, impressa em 3D com efeito de luz vibrante e personalizável. Perfeita para quartos, salas gamer ou presentes especiais, ela combina tecnologia e estilo em um produto único. Peça a sua agora e acenda o ambiente!',
 69.90, '/imagem-produtos/img8.jpg', true, 'Decoração', 'PLA Translúcido', 12, 0),

('Porta Chaves',
 'Porta chaves criativo com suporte para 4 chaves, design moderno impresso em 3D para fixar na parede.',
 'Organize suas chaves com estilo usando o Porta Chaves 3D, um acessório moderno e prático feito sob medida para sua entrada. Com suporte para até 4 chaves e design minimalista, ele combina organização e decoração. Fixação simples na parede — peça já o seu!',
 34.90, '/imagem-produtos/portachave.jpg', true, 'Decoração', 'PLA', 20, 0),

('Suporte para Celular',
 'Suporte articulado para celular impresso em 3D, compatível com todos os modelos de smartphone.',
 'Facilite seu dia a dia com o Suporte para Celular 3D, um acessório prático e resistente compatível com qualquer smartphone. Ideal para home office, cozinha ou mesa de trabalho, ele mantém seu celular sempre na posição certa. Leve e durável — adicione ao seu carrinho agora!',
 29.90, '/imagem-produtos/portacelular.jpg', false, 'Acessórios', 'PETG', 25, 0),

('Suporte para Controle',
 'Suporte para controle de videogame impresso em 3D, compatível com Xbox, PlayStation e controles genéricos.',
 'Mantenha seu controle sempre à mão com o Suporte para Controle 3D, projetado para Xbox, PlayStation e demais modelos. Com design ergonômico e acabamento premium, ele organiza seu setup gamer com estilo. Garanta o seu e eleve seu espaço gamer!',
 44.90, '/imagem-produtos/img7.jpg', false, 'Acessórios', 'PLA', 18, 0),

('Suporte Cristal Xbox',
 'Suporte decorativo em formato cristal para controle Xbox, com design translúcido e iluminado.',
 'Exiba seu controle Xbox com elegância usando o Suporte Cristal, um acessório exclusivo com design translúcido que valoriza qualquer setup. Feito em PLA cristal de alta qualidade, ele une funcionalidade e decoração de forma única. Personalize seu espaço gamer — peça o seu!',
 54.90, '/imagem-produtos/suportecristalxbox.jpg', true, 'Acessórios', 'PLA Cristal', 8, 0),

('Colar Espinha',
 'Colar articulado em formato de espinha dorsal, impresso em 3D com acabamento detalhado e flexível.',
 'Use um acessório que chama atenção com o Colar Espinha 3D, uma peça articulada e flexível com acabamento premium. Perfeito para quem ama estilo alternativo e único, ele é impresso sob demanda para garantir o melhor caimento. Destaque-se — adicione ao seu estilo agora!',
 49.90, '/imagem-produtos/colarespinha.jpg', true, 'Acessórios', 'TPU Flexível', 10, 0),

('Chaveiro CSGO',
 'Chaveiro temático de Counter-Strike com detalhes de armas e logo do jogo, impresso em 3D.',
 'Leve um pedaço do jogo para o dia a dia com o Chaveiro CSGO 3D, um item colecionável com detalhes fiéis ao universo do Counter-Strike. Leve, resistente e perfeito para presentear fãs do jogo. Compre agora e represente seu game favorito!',
 19.90, '/imagem-produtos/chaveiroCSGO.jpg', false, 'Acessórios', 'PLA', 30, 0),

('Chaveiro LOL',
 'Chaveiro temático de League of Legends com personagens e logo do jogo, impresso em 3D com pintura manual.',
 'Mostre sua paixão pelo League of Legends com o Chaveiro LOL 3D, um item exclusivo com personagens detalhados e acabamento colorido. Leve e durável, é o presente perfeito para qualquer fã do jogo. Invoque o seu agora!',
 19.90, '/imagem-produtos/chaveirolol.jpg', false, 'Acessórios', 'PLA', 30, 0),

('Capivara Gamer',
 'Miniatura colecionável de capivara no estilo gamer, impressa em 3D com detalhes de headset e controle.',
 'A Capivara Gamer 3D é o colecionável que todo gamer precisa ter — fofa, detalhada e com o estilo gamer que você ama. Impressa em PLA de alta qualidade com pintura artesanal, ela é perfeita para decorar sua mesa ou setup. Leve a sua bicharada para casa!',
 39.90, '/imagem-produtos/capivaraGamer.jpg', true, 'Colecionáveis', 'PLA', 15, 0),

('Miniatura Guerreiro',
 'Miniatura colecionável de guerreiro RPG com armadura detalhada, impressa em 3D com pintura artesanal.',
 'Colecione ou presentes com a Miniatura Guerreiro RPG 3D, uma peça impressa com detalhes incríveis de armadura e espada, ideal para fãs de RPG e fantasia. Cada miniatura é pintada artesanalmente para garantir unicidade. Adicione ao seu reino — peça a sua!',
 49.90, '/imagem-produtos/img3.jpg', true, 'Colecionáveis', 'Resina', 10, 0),

('Dragão Suporte',
 'Suporte decorativo em formato de dragão, impresso em 3D com detalhes em escamas e acabamento fosco.',
 'O Dragão Suporte 3D é a peça colecionável que une arte e funcionalidade — um dragão detalhado que serve como suporte para objetos de mesa. Feito em PLA com acabamento fosco premium, ele é perfeito para decorar setups e prateleiras. Traga o dragão para o seu espaço!',
 79.90, '/imagem-produtos/dragão suporte.jpg', false, 'Colecionáveis', 'PLA', 8, 0),

('Sonic',
 'Miniatura colecionável do Sonic em pose de corrida, impressa em 3D com pintura artesanal nas cores oficiais.',
 'Acelere sua coleção com a Miniatura Sonic 3D, fiel ao personagem clássico da SEGA com detalhes e cores vibrantes pintados à mão. Perfeita para fãs nostálgicos e colecionadores de todas as idades. Gotta go fast — garanta a sua!',
 44.90, '/imagem-produtos/sonic.jpg', false, 'Colecionáveis', 'PLA', 12, 0),

('Silksong',
 'Miniatura colecionável da Hornet do jogo Hollow Knight: Silksong, impressa em 3D com detalhes artesanais.',
 'Honre a guerreira Hornet com a Miniatura Silksong 3D, uma peça exclusiva para fãs do universo Hollow Knight com detalhes impressionantes. Impressa em resina de alta resolução e pintada à mão, ela é um item raro para qualquer coleção. Seja o primeiro a ter a sua!',
 59.90, '/imagem-produtos/silksong.jpg', false, 'Colecionáveis', 'Resina', 6, 0),

('Organizador de Mesa',
 'Organizador modular para mesa de trabalho com compartimentos para canetas, clips e acessórios.',
 'Mantenha sua mesa sempre organizada com o Organizador 3D, um produto modular e personalizável com compartimentos para todos os seus acessórios. Feito em PLA resistente, ele combina praticidade e design no seu espaço de trabalho. Organize seu ambiente — peça o seu!',
 39.90, '/imagem-produtos/img5.jpg', true, 'Acessórios', 'PLA', 20, 0),

('Suporte para Ferramentas',
 'Suporte organizador para ferramentas pequenas impresso em 3D, ideal para bancadas e ateliês.',
 'Organize suas ferramentas com eficiência usando o Suporte 3D para Ferramentas, projetado para bancadas e ateliês com compartimentos modulares. Resistente em PETG e personalizável no tamanho, ele é essencial para quem trabalha com precisão. Otimize seu espaço agora!',
 59.90, '/imagem-produtos/img4.jpg', false, 'Acessórios', 'PETG', 15, 0),

('Carrinho Polícia 3D',
 'Miniatura de carrinho de polícia impresso em 3D com detalhes realistas e pintura artesanal.',
 'O Carrinho Polícia 3D é uma miniatura colecionável com detalhes realistas de viatura, perfeita para colecionadores e crianças que amam carrinhos. Feito em PLA resistente com pintura artesanal, ele é seguro e durável. Adicione à sua garagem — peça o seu!',
 34.90, '/imagem-produtos/img6.jpg', false, 'Colecionáveis', 'PLA', 20, 0);