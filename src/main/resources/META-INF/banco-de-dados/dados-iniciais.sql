insert into produto (id, nome, preco, data_criacao, descricao) values (1, 'Kindle', 799.0, date_sub(sysdate(), interval 1 day), 'Conheça o novo Kindle, agora com iluminação embutida ajustável, que permite que você leia em ambientes abertos ou fechados, a qualquer hora do dia.');
insert into produto (id, nome, preco, data_criacao, descricao) values (3, 'Câmero GoPro Hero 7', 1500.0, date_sub(sysdate(), interval 1 day), 'Desempenho 2x melhor em comparação as cameras normais.');
insert into produto (id, nome, preco, data_criacao, descricao) values (4, 'Tablet Samsung', 1500.0, date_sub(sysdate(), interval 1 day), 'Desempenho 2x melhor em comparação as cameras normais.');
insert into produto (id, nome, preco, data_criacao, descricao) values (5, 'Câmera Canon 80D', 3500.0, sysdate(), 'O melhor ajuste de foco.');
insert into produto (id, nome, preco, data_criacao, descricao) values (6, 'Microfone de Lapela', 50.0, sysdate(), 'Produto massa.');

insert into cliente (id, nome, cpf) values (1, "Fernando Medeiros", "123456");
insert into cliente (id, nome, cpf) values (2, "Marcos Mariano", "654321");
insert into cliente (id, nome, cpf) values (3, "Arthur Basilio", "456789");
insert into cliente_detalhe (cliente_id, sexo, data_nascimento) values (1, "MASCULINO", date_sub(sysdate(), interval 27 year));
insert into cliente_detalhe (cliente_id, sexo, data_nascimento) values (2, "MASCULINO", date_sub(sysdate(), interval 30 year));

insert into pedido (id, cliente_id, total, data_criacao, status) values (1, 1, 2398.0, sysdate(), 'AGUARDANDO');
insert into pedido (id, cliente_id, total, data_criacao, status) values (2, 1, 499.0, date_sub(sysdate(), interval 5 day), 'CANCELADO');
insert into pedido (id, cliente_id, total, data_criacao, status) values (3, 3, 3000.0, date_sub(sysdate(), interval 1 day), 'PAGO');
insert into pedido (id, cliente_id, total, data_criacao, status) values (4, 1, 3500.0, date_sub(sysdate(), interval 2 month), 'PAGO');
insert into pedido (id, cliente_id, total, data_criacao, status) values (5, 2, 499.0, date_sub(sysdate(), interval 1 month), 'PAGO');
insert into pedido (id, cliente_id, total, data_criacao, status) values (6, 1, 799.0, date_sub(sysdate(), interval 2 day), 'PAGO');
insert into pedido (id, cliente_id, total, data_criacao, status) values (7, 2, 1500.0, sysdate(), 'AGUARDANDO');

insert into nota_fiscal(pedido_id, xml, data_emissao) values (3, '<xml />', sysdate())

insert into item_pedido (pedido_id, produto_id, preco_produto, quantidade) values (1, 3, 1400.0, 1);
insert into item_pedido (pedido_id, produto_id, preco_produto, quantidade) values (1, 1, 499.0, 2);
insert into item_pedido (pedido_id, produto_id, preco_produto, quantidade) values (2, 1, 499.0, 1);
insert into item_pedido (pedido_id, produto_id, preco_produto, quantidade) values (3, 4, 1500.0, 2);
insert into item_pedido (pedido_id, produto_id, preco_produto, quantidade) values (4, 5, 3500, 1);
insert into item_pedido (pedido_id, produto_id, preco_produto, quantidade) values (5, 1, 499, 1);
insert into item_pedido (pedido_id, produto_id, preco_produto, quantidade) values (6, 1, 799, 1);
insert into item_pedido (pedido_id, produto_id, preco_produto, quantidade) values (7, 4, 1500.0, 1);

insert into pagamento (pedido_id, status, codigo_barras, tipo_pagamento, data_vencimento) values (1, 'PROCESSANDO', '8889999966666666', 'boleto', date_add(sysdate(), interval 3 day));
insert into pagamento (pedido_id, status, numero_cartao, tipo_pagamento, data_vencimento) values (3, 'RECEBIDO', '5555-4444-3333-1111', 'cartao', null);
insert into pagamento (pedido_id, status, codigo_barras, tipo_pagamento, data_vencimento) values (4, 'RECEBIDO', '8889999966666667', 'boleto', date_sub(sysdate(), interval 28 day));
insert into pagamento (pedido_id, status, numero_cartao, tipo_pagamento, data_vencimento) values (5, 'RECEBIDO', '5555-6666-3333-1111', 'cartao', null);
insert into pagamento (pedido_id, status, codigo_barras, tipo_pagamento, data_vencimento) values (7, 'PROCESSANDO', '8889999966666668', 'boleto', date_add(sysdate(), interval 2 day));

insert into categoria (id, nome) values (1, 'Eletrônicos');
insert into categoria (id, nome) values (2, 'Livros');
insert into categoria (id, nome) values (3, 'Esportes');
insert into categoria (id, nome) values (4, 'Futebol');
insert into categoria (id, nome) values (5, 'Natação');
insert into categoria (id, nome) values (6, 'Notebooks');
insert into categoria (id, nome) values (7, 'Smathphones');
insert into categoria (id, nome) values (8, 'Câmeras');

insert into produto_categoria (produto_id, categoria_id) values (1, 2);
insert into produto_categoria (produto_id, categoria_id) values (3, 8);
insert into produto_categoria (produto_id, categoria_id) values (4, 7);
insert into produto_categoria (produto_id, categoria_id) values (5, 8);