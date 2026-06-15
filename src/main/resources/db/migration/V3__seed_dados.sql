-- usuarios (1 ADMIN + 4 CLIENTE)
INSERT INTO usuarios (nome, email, senha, telefone, cpf, role, ativo, criado_em, atualizado_em) VALUES
('Admin MarcaPasso', 'admin@marcapasso.com', '$2a$10$abcdefghijklmnopqrstuuVwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ12', '81999990000', '000.000.000-00', 'ADMIN', true, NOW(), NOW()),
('João Silva', 'joao@email.com', '$2a$10$abcdefghijklmnopqrstuuVwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ12', '81988881111', '111.111.111-11', 'CLIENTE', true, NOW(), NOW()),
('Maria Oliveira', 'maria@email.com', '$2a$10$abcdefghijklmnopqrstuuVwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ12', '81977772222', '222.222.222-22', 'CLIENTE', true, NOW(), NOW()),
('Carlos Santos', 'carlos@email.com', '$2a$10$abcdefghijklmnopqrstuuVwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ12', '81966663333', '333.333.333-33', 'CLIENTE', true, NOW(), NOW()),
('Ana Ferreira', 'ana@email.com', '$2a$10$abcdefghijklmnopqrstuuVwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ12', '81955554444', '444.444.444-44', 'CLIENTE', true, NOW(), NOW());

-- favoritos (1 por usuario CLIENTE)
INSERT INTO favoritos (id_usuario) VALUES (2), (3), (4), (5);

-- favorito_produtos
INSERT INTO favorito_produtos (id_favorito, id_produto) VALUES
(1, 1), (1, 3),
(2, 2), (2, 5),
(3, 7), (3, 8),
(4, 4), (4, 6);

-- enderecos
INSERT INTO enderecos (id_usuario, nome, rua, numero, complemento, bairro, cidade, estado, cep) VALUES
(2, 'Casa', 'Rua das Flores', '123', 'Apto 1', 'Centro', 'Recife', 'PE', '50000-000'),
(3, 'Casa', 'Av. Boa Viagem', '456', NULL, 'Boa Viagem', 'Recife', 'PE', '51000-000'),
(4, 'Trabalho', 'Rua do Riachuelo', '789', 'Sala 5', 'Santo Antônio', 'Recife', 'PE', '50020-000'),
(5, 'Casa', 'Rua Nova', '321', NULL, 'Espinheiro', 'Recife', 'PE', '52020-000'),
(1, 'Sede', 'Av. Agamenon Magalhães', '1000', NULL, 'Derby', 'Recife', 'PE', '52010-000');

-- cupons
INSERT INTO cupons (nome_cupom, valor_desconto, tipo_validade, data_expiracao, criado_em, atualizado_em) VALUES
('BEMVINDO10', 10.00, 'PERCENTUAL', '2026-12-31', NOW(), NOW()),
('FRETE20', 20.00, 'FIXO', '2026-09-30', NOW(), NOW()),
('GAMER15', 15.00, 'PERCENTUAL', '2026-08-01', NOW(), NOW()),
('NATAL25', 25.00, 'PERCENTUAL', '2026-12-25', NOW(), NOW()),
('PROMO5', 5.00, 'FIXO', '2026-07-01', NOW(), NOW());

-- carrinhos
INSERT INTO carrinhos (id_usuario, criado_em) VALUES
(2, NOW()), (3, NOW()), (4, NOW()), (5, NOW()), (1, NOW());

-- carrinho_itens
INSERT INTO carrinho_itens (id_carrinho, id_produto, quantidade, preco_unitario) VALUES
(1, 1, 2, 89.90),
(1, 3, 1, 69.90),
(2, 5, 1, 29.90),
(3, 7, 1, 54.90),
(4, 2, 3, 74.90);

-- encomendas
INSERT INTO encomendas (id_usuario, numero_pedido, data_hora, status, subtotal, frete, desconto, desconto_cupom, total, forma_pagamento, cliente_nome, cliente_email, cliente_cpf, end_rua, end_numero, end_bairro, end_cidade, end_estado, end_cep) VALUES
(2, 'PED-001', NOW(), 'ENTREGUE',  179.80, 15.00, 0.00, 0.00, 194.80, 'CARTAO', 'João Silva',   'joao@email.com',   '111.111.111-11', 'Rua das Flores',        '123', 'Centro',       'Recife', 'PE', '50000-000'),
(3, 'PED-002', NOW(), 'ENVIADO',   74.90,  10.00, 0.00, 7.49, 77.41,  'PIX',    'Maria Oliveira','maria@email.com',  '222.222.222-22', 'Av. Boa Viagem',        '456', 'Boa Viagem',   'Recife', 'PE', '51000-000'),
(4, 'PED-003', NOW(), 'PENDENTE',  54.90,  12.00, 0.00, 0.00, 66.90,  'BOLETO', 'Carlos Santos', 'carlos@email.com', '333.333.333-33', 'Rua do Riachuelo',      '789', 'Santo Antônio','Recife', 'PE', '50020-000'),
(5, 'PED-004', NOW(), 'CANCELADO', 89.90,  0.00,  0.00, 0.00, 89.90,  'PIX',    'Ana Ferreira',  'ana@email.com',    '444.444.444-44', 'Rua Nova',              '321', 'Espinheiro',   'Recife', 'PE', '52020-000'),
(2, 'PED-005', NOW(), 'PROCESSANDO',49.90, 8.00,  5.00, 0.00, 52.90,  'CARTAO', 'João Silva',   'joao@email.com',   '111.111.111-11', 'Rua das Flores',        '123', 'Centro',       'Recife', 'PE', '50000-000');

-- encomenda_itens
INSERT INTO encomenda_itens (id_encomenda, id_produto, quantidade, preco_unitario) VALUES
(1, 1, 2, 89.90),
(2, 2, 1, 74.90),
(3, 7, 1, 54.90),
(4, 1, 1, 89.90),
(5, 8, 1, 49.90);