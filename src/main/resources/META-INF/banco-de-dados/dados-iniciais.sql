-- Alterado/removido: sysdate, date_sub, xml da nota_fiscal

insert into produto (id, versao, nome, preco, data_criacao, ativo, descricao) values (1, 0, 'Kindle', 799.0, (now() - interval '1 day'), 'SIM', 'Conheça o novo Kindle, agora com iluminação embutida ajustável, que permite que você leia em ambientes abertos ou fechados, a qualquer hora do dia.');
insert into produto (id, versao, nome, preco, data_criacao, ativo, descricao) values (3, 0, 'Câmero GoPro Hero 7', 1500.0, (now() - interval '1 day'), 'SIM', 'Desempenho 2x melhor em comparação as cameras normais.');
insert into produto (id, versao, nome, preco, data_criacao, ativo, descricao) values (4, 0, 'Tablet Samsung', 1500.0, (now() - interval '1 day'), 'SIM', 'Desempenho 2x melhor em comparação as cameras normais.');
insert into produto (id, versao, nome, preco, data_criacao, ativo, descricao) values (5, 0, 'Câmera Canon 80D', 3500.0, now(), 'NAO', 'O melhor ajuste de foco.');
insert into produto (id, versao, nome, preco, data_criacao, ativo, descricao) values (6, 0, 'Microfone de Lapela', 50.0, now(), 'NAO', 'Produto massa.');

insert into cliente (id, versao, nome, cpf) values (1, 0, 'Fernando Medeiros', '123456');
insert into cliente (id, versao, nome, cpf) values (2, 0, 'Marcos Mariano', '654321');
insert into cliente (id, versao, nome, cpf) values (3, 0, 'Arthur Basilio', '456789');
insert into cliente_detalhe (cliente_id, sexo, data_nascimento) values (1, 'MASCULINO', (now() - interval '27 year'));
insert into cliente_detalhe (cliente_id, sexo, data_nascimento) values (2, 'MASCULINO', (now() - interval '30 year'));

insert into pedido (id, versao, cliente_id, total, data_criacao, status) values (1, 0, 1, 2398.0, now(), 'AGUARDANDO');
insert into pedido (id, versao, cliente_id, total, data_criacao, status) values (2, 0, 1, 499.0, (now() - interval '5 day'), 'CANCELADO');
insert into pedido (id, versao, cliente_id, total, data_criacao, status) values (3, 0, 3, 3000.0, (now() - interval '1 day'), 'PAGO');
insert into pedido (id, versao, cliente_id, total, data_criacao, status) values (4, 0, 1, 3500.0, (now() - interval '2 month'), 'PAGO');
insert into pedido (id, versao, cliente_id, total, data_criacao, status) values (5, 0, 2, 499.0, (now() - interval '1 month'), 'PAGO');
insert into pedido (id, versao, cliente_id, total, data_criacao, status) values (6, 0, 1, 799.0, (now() - interval '2 day'), 'PAGO');
insert into pedido (id, versao, cliente_id, total, data_criacao, status) values (7, 0, 2, 1500.0, now(), 'AGUARDANDO');

insert into nota_fiscal(pedido_id, versao, xml, data_emissao) values (3, 0, '<xml />'::bytea, now())

insert into item_pedido (pedido_id, produto_id, versao, preco_produto, quantidade) values (1, 3, 0, 1400.0, 1);
insert into item_pedido (pedido_id, produto_id, versao, preco_produto, quantidade) values (1, 1, 0, 499.0, 2);
insert into item_pedido (pedido_id, produto_id, versao, preco_produto, quantidade) values (2, 1, 0, 499.0, 1);
insert into item_pedido (pedido_id, produto_id, versao, preco_produto, quantidade) values (3, 4, 0, 1500.0, 2);
insert into item_pedido (pedido_id, produto_id, versao, preco_produto, quantidade) values (4, 5, 0, 3500, 1);
insert into item_pedido (pedido_id, produto_id, versao, preco_produto, quantidade) values (5, 1, 0, 499, 1);
insert into item_pedido (pedido_id, produto_id, versao, preco_produto, quantidade) values (6, 1, 0, 799, 1);
insert into item_pedido (pedido_id, produto_id, versao, preco_produto, quantidade) values (7, 4, 0, 1500.0, 1);

insert into pagamento (pedido_id, versao, status, codigo_barras, tipo_pagamento, data_vencimento) values (1, 0, 'PROCESSANDO', '8889999966666666', 'boleto', (now() + interval '3 day'));
insert into pagamento (pedido_id, versao, status, numero_cartao, tipo_pagamento, data_vencimento) values (3, 0, 'RECEBIDO', '5555-4444-3333-1111', 'cartao', null);
insert into pagamento (pedido_id, versao, status, codigo_barras, tipo_pagamento, data_vencimento) values (4, 0, 'RECEBIDO', '8889999966666667', 'boleto', (now() + interval '28 day'));
insert into pagamento (pedido_id, versao, status, numero_cartao, tipo_pagamento, data_vencimento) values (5, 0, 'RECEBIDO', '5555-6666-3333-1111', 'cartao', null);
insert into pagamento (pedido_id, versao, status, codigo_barras, tipo_pagamento, data_vencimento) values (7, 0, 'PROCESSANDO', '8889999966666668', 'boleto', (now() + interval '2 day'));

insert into categoria (id, versao, nome) values (1, 0, 'Eletrônicos');
insert into categoria (id, versao, nome) values (2, 0, 'Livros');
insert into categoria (id, versao, nome) values (3, 0, 'Esportes');
insert into categoria (id, versao, nome) values (4, 0, 'Futebol');
insert into categoria (id, versao, nome) values (5, 0, 'Natação');
insert into categoria (id, versao, nome) values (6, 0, 'Notebooks');
insert into categoria (id, versao, nome) values (7, 0, 'Smathphones');
insert into categoria (id, versao, nome) values (8, 0, 'Câmeras');

insert into produto_categoria (produto_id, categoria_id) values (1, 2);
insert into produto_categoria (produto_id, categoria_id) values (3, 8);
insert into produto_categoria (produto_id, categoria_id) values (4, 7);
insert into produto_categoria (produto_id, categoria_id) values (5, 8);

insert into produto_loja(id, nome, preco, data_criacao, descricao) values (101, 'Kindle', 799.0, (now() - interval '1 day'), 'Conheça o novo Kindle, agora com iluminação embutida ajustável, que permite que você leia em ambientes abertos ou fechados, a qualquer hora do dia.');
insert into produto_loja(id, nome, preco, data_criacao, descricao) values (103, 'Câmero GoPro Hero 7', 1500.0, (now() - interval '1 day'), 'Desempenho 2x melhor em comparação as cameras normais.');
insert into produto_loja(id, nome, preco, data_criacao, descricao) values (104, 'Tablet Samsung', 1500.0, (now() - interval '1 day'), 'Desempenho 2x melhor em comparação as cameras normais.');
insert into produto_loja(id, nome, preco, data_criacao, descricao) values (105, 'Câmera Canon 80D', 3500.0, now(), 'O melhor ajuste de foco.');
insert into produto_loja(id, nome, preco, data_criacao, descricao) values (106, 'Microfone de Lapela', 50.0, now(), 'Produto massa.');

insert into ecm_produto(prd_id, prd_nome, prd_preco, prd_data_criacao, prd_descricao) values (201, 'Kindle', 799.0, (now() - interval '1 day'), 'Conheça o novo Kindle, agora com iluminação embutida ajustável, que permite que você leia em ambientes abertos ou fechados, a qualquer hora do dia.');
insert into ecm_produto(prd_id, prd_nome, prd_preco, prd_data_criacao, prd_descricao) values (203, 'Câmero GoPro Hero 7', 1500.0, (now() - interval '1 day'), 'Desempenho 2x melhor em comparação as cameras normais.');
insert into ecm_produto(prd_id, prd_nome, prd_preco, prd_data_criacao, prd_descricao) values (204, 'Tablet Samsung', 1500.0, (now() - interval '1 day'), 'Desempenho 2x melhor em comparação as cameras normais.');
insert into ecm_produto(prd_id, prd_nome, prd_preco, prd_data_criacao, prd_descricao) values (205, 'Câmera Canon 80D', 3500.0, now(), 'O melhor ajuste de foco.');
insert into ecm_produto(prd_id, prd_nome, prd_preco, prd_data_criacao, prd_descricao) values (206, 'Microfone de Lapela', 50.0, now(), 'Produto massa.');

insert into erp_produto(id, nome, preco, descricao) values (301, 'Kindle', 799.0, 'Conheça o novo Kindle, agora com iluminação embutida ajustável, que permite que você leia em ambientes abertos ou fechados, a qualquer hora do dia.');
insert into erp_produto(id, nome, preco, descricao) values (303, 'Câmero GoPro Hero 7', 1500.0, 'Desempenho 2x melhor em comparação as cameras normais.');
insert into erp_produto(id, nome, preco, descricao) values (304, 'Tablet Samsung', 1500.0, 'Desempenho 2x melhor em comparação as cameras normais.');
insert into erp_produto(id, nome, preco, descricao) values (305, 'Câmera Canon 80D', 3500.0, 'O melhor ajuste de foco.');
insert into erp_produto(id, nome, preco, descricao) values (306, 'Microfone de Lapela', 50.0, 'Produto massa.');

insert into ecm_categoria (cat_id, cat_nome) values (201, 'Eletrodomésticos');
insert into ecm_categoria (cat_id, cat_nome) values (202, 'Livros');
insert into ecm_categoria (cat_id, cat_nome) values (203, 'Esportes');
insert into ecm_categoria (cat_id, cat_nome) values (204, 'Futebol');
insert into ecm_categoria (cat_id, cat_nome) values (205, 'Natação');
insert into ecm_categoria (cat_id, cat_nome) values (206, 'Notebooks');
insert into ecm_categoria (cat_id, cat_nome) values (207, 'Smartphones');
insert into ecm_categoria (cat_id, cat_nome) values (208, 'Câmeras');