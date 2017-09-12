package com.candao.trace.framework.jpa.factory;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import com.candao.trace.framework.jpa.repository.base.impl.BaseRepositoryImpl;

public class BaseRepositoryFactoryBean<T extends JpaRepository<S, ID>, S, ID extends Serializable>   extends JpaRepositoryFactoryBean<T,S,ID> {

    public BaseRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
		super(repositoryInterface);
	}

	@Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager)    {
        return new BaseRepositoryFactory(entityManager);
    }
}

class BaseRepositoryFactory extends JpaRepositoryFactory {
    public BaseRepositoryFactory(EntityManager entityManager){
        super(entityManager);
    }


    //指定实现类
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    protected <T, ID extends Serializable> SimpleJpaRepository<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
        BaseRepositoryImpl customRepository = new BaseRepositoryImpl<T,ID>((Class<T>)information.getDomainType(),entityManager);
        return customRepository;

    }

    //指定实现类类型
    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata){
        return BaseRepositoryImpl.class;
    }
}