//
//  RepositoryDetailInfoViewController.swift
//  GitHubClient
//
//  Created by Ilya Shevtsov on 23.06.2022.
//

import UIKit
import SafariServices
import MaterialComponents.MaterialActivityIndicator
import shared
import SwiftyMarkdown


class RepositoryDetailInfoViewController: UIViewController {
    @IBOutlet private weak var linkLabel: UILabel!
    
    @IBOutlet private weak var licenseLabelWithValue: UILabel!
    @IBOutlet private weak var watchersLabelWithValue: UILabel!
    @IBOutlet private weak var forksLabelWithValue: UILabel!
    @IBOutlet private weak var starsLabelWithValue: UILabel!
    
    @IBOutlet private weak var licenseLabel: UILabel!
    @IBOutlet private weak var starsLabel: UILabel!
    @IBOutlet private weak var watchersLabel: UILabel!
    @IBOutlet private weak var forksLabel: UILabel!
    @IBOutlet private weak var readmeLabel: UILabel!
    @IBOutlet private weak var contentStackView: UIStackView!
    
    @IBOutlet private weak var errorView: ErrorView!
    @IBOutlet private weak var readmeErrorView: ErrorView!
    
    @IBOutlet private weak var activityIndicator: MDCActivityIndicator!
    @IBOutlet private weak var readmeActivityIndicator: MDCActivityIndicator!
    
    private var repo: Repo
    private let appRepo = DI.shared.appRepo
    
    init(repo: Repo, nibName: String?, bundle: Bundle?) {
        self.repo = repo
        super.init(nibName: nibName, bundle: bundle)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setUI()
        getRepoInfo()
        getReadmeAndUpdateUI()
    }
    
    private func getRepoInfo() {
        loadingStart(content: contentStackView, errorView: errorView, indicator: activityIndicator)
        appRepo.getRepository(ownerName: repo.owner, repoName: repo.name) { [weak self] repo, error in
            self?.activityIndicator.hide()
            if let error = error {
                self?.contentStackView.isHidden = true
//                self?.showErrorView(self?.errorView, error: error) { self?.getRepoInfo() }
                return
            }
//            self?.hideErrorView(self?.errorView)
            if let repo = repo {
                self?.repo = repo
                self?.updateRepoInfoUI()
            }
            self?.contentStackView.isHidden = false
        }
    }
    
    private func getReadmeAndUpdateUI() {
        loadingStart(content: readmeLabel, errorView: readmeErrorView, indicator: readmeActivityIndicator)
        appRepo.getRepositoryReadme(ownerName: repo.owner, repoName: repo.name, branchName: repo.branch){ [weak self] (readme, error) in
            self?.readmeActivityIndicator.hide()
//            if let error = error {
//                if case .readmeNotFound = error {
//                    self?.readmeLabel.text = error.errorDescription
//                    self?.readmeLabel.isHidden = false
//                    return
//                }
//                self?.showErrorView(self?.readmeErrorView, error: error) { self?.getReadmeAndUpdateUI() }
//                return
//            }
//            self?.hideErrorView(self?.readmeErrorView)
            self?.readmeLabel.isHidden = false
            if (readme ?? "").isEmpty {
                self?.readmeLabel.text = NSLocalizedString("repoDetails.readmeLabel.emptyReadme.title", comment: "")
                return
            }
            let md = SwiftyMarkdown(string: readme!)
            self?.readmeLabel.attributedText = md.attributedString()
        }
    }
    
    private func updateRepoInfoUI(){
        title = repo.name
        linkLabel.text = repo.htmlUrl
        licenseLabelWithValue.text = repo.license ?? NSLocalizedString("repoDetails.licenseLabelWithValue.noLicense.title", comment: "")
        starsLabelWithValue.text = "\(repo.stars)"
        forksLabelWithValue.text = "\(repo.forks)"
        watchersLabelWithValue.text = "\(repo.watchers)"
    }
    
    private func setUI(){
        setExitButton()
        
        linkLabel.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(self.linkPressed)))
        
        activityIndicator.setColor()
        activityIndicator.radius = 28
        
        readmeActivityIndicator.setColor()
        readmeActivityIndicator.radius = 12
        
        licenseLabel.text = NSLocalizedString("repoDetails.licenseLabel.title", comment: "")
        starsLabel.text = NSLocalizedString("repoDetails.starsLabel.title", comment: "")
        forksLabel.text = NSLocalizedString("repoDetails.forksLabel.title", comment: "")
        watchersLabel.text = NSLocalizedString("repoDetails.watchersLabel.title", comment: "")
    }
    
    private func loadingStart(content: UIView, errorView: ErrorView, indicator: MDCActivityIndicator) {
        content.isHidden = true
//        hideErrorView(errorView)
        indicator.show()
    }
    
    @IBAction private func linkPressed() {
        if let url = URL(string: linkLabel.text!) {
            let safariViewController = SFSafariViewController(url: url)
            present(safariViewController, animated: true,
                    completion: nil)
        }
    }
}
