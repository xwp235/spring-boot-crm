DROP TABLE IF EXISTS public.mast_config_category;
CREATE TABLE public.mast_config_category (
    id INT4 GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    parent_id INT4 NOT NULL,
    parent_name VARCHAR(16) NOT NULL,
    name VARCHAR(16) NOT NULL,
    code VARCHAR(16) NOT NULL,
    description VARCHAR(60) NOT NULL DEFAULT '',
    editable BOOL NOT NULL DEFAULT true,
    deletable BOOL NOT NULL DEFAULT true,
    create_time timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by int4 NOT NULL,
    last_updated_by int4 NOT NULL,
    CONSTRAINT unq_mcc_code UNIQUE (code)
)
WITH (OIDS = false)
TABLESPACE ts_spring_boot_crm;

COMMENT ON TABLE public.mast_config_category IS '系统配置分类表';
COMMENT ON COLUMN public.mast_config_category.id IS '主键';
COMMENT ON COLUMN public.mast_config_category.parent_id IS '父级分类id';
COMMENT ON COLUMN public.mast_config_category.parent_name IS '父级分类名称';
COMMENT ON COLUMN public.mast_config_category.name IS  '分类名称';
COMMENT ON COLUMN public.mast_config_category.code IS  '分类唯一code';
COMMENT ON COLUMN public.mast_config_category.description IS  '分类描述';
COMMENT ON COLUMN public.mast_config_category.editable IS '是否可被再次编辑';
COMMENT ON COLUMN public.mast_config_category.deletable IS '是否可被删除';
COMMENT ON COLUMN public.mast_config_category.create_time IS '创建时间';
COMMENT ON COLUMN public.mast_config_category.update_time IS '修改时间';
COMMENT ON COLUMN public.mast_config_category.created_by IS '创建人ID';
COMMENT ON COLUMN public.mast_config_category.last_updated_by IS '最后更新人ID';


DROP TABLE IF EXISTS public.mast_config;
DROP TABLE IF EXISTS public.mast_config;
CREATE TABLE public.mast_config (
    id INT4 GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cate_id INT4 NOT NULL,
    prop_key VARCHAR(16) NOT NULL,
    prop_value TEXT NOT NULL,
    prop_type VARCHAR(16) NOT NULL,
    description VARCHAR(60) NOT NULL DEFAULT '',
    editable BOOL NOT NULL DEFAULT true,
    deletable BOOL NOT NULL DEFAULT true,
    create_time timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by int4 NOT NULL,
    last_updated_by int4 NOT NULL,
    CONSTRAINT unq_mc_cate_id_prop_key UNIQUE (cate_id,prop_key)
)
WITH (OIDS = false)
TABLESPACE ts_spring_boot_crm;

COMMENT ON TABLE public.mast_config IS '系统配置表';
COMMENT ON COLUMN public.mast_config.id IS '主键';
COMMENT ON COLUMN public.mast_config.cate_id IS '分类id';
COMMENT ON COLUMN public.mast_config.prop_key IS  '配置属性名';
COMMENT ON COLUMN public.mast_config.prop_value IS  '配置属性值';
COMMENT ON COLUMN public.mast_config.prop_type IS  '配置数据类型，可以是:json,int,string,date,datetime,double,boolean';
COMMENT ON COLUMN public.mast_config.description IS  '配置说明';
COMMENT ON COLUMN public.mast_config.editable IS '是否可被再次编辑';
COMMENT ON COLUMN public.mast_config.deletable IS '是否可被删除';
COMMENT ON COLUMN public.mast_config.create_time IS '创建时间';
COMMENT ON COLUMN public.mast_config.update_time IS '修改时间';
COMMENT ON COLUMN public.mast_config.created_by IS '创建人ID';
COMMENT ON COLUMN public.mast_config.last_updated_by IS '最后更新人ID';


CREATE OR REPLACE FUNCTION notify_table_change()
RETURNS TRIGGER AS $$
BEGIN
    PERFORM pg_notify(
        'table_changes',  -- 通知频道名称
        json_build_object(
            'operation', TG_OP,              -- 操作类型（INSERT/UPDATE/DELETE）
            'table', TG_TABLE_NAME,          -- 变化的表名
            'old_data', row_to_json(OLD),    -- 修改前数据（针对UPDATE/DELETE）
            'new_data', row_to_json(NEW)     -- 修改后数据（针对INSERT/UPDATE）
        )::text
    );
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trigger_config_change ON public.mast_config;
CREATE TRIGGER trigger_config_change
    AFTER INSERT OR UPDATE OR DELETE ON public.mast_config
FOR EACH ROW EXECUTE FUNCTION notify_table_change();
